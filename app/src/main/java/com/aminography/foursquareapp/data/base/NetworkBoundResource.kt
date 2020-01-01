package com.aminography.foursquareapp.data.base

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.flow.asFlow
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A generic class that can provide a resource backed by both the database and the network.
 *
 * @param <ResultType> channel type of the resource
 * @param <ResponseType> channel type of the webservice response
 * @param <QueryType> type of the query object
 *
 * @author aminography
 */
@Suppress("EXPERIMENTAL_API_USAGE")
abstract class NetworkBoundResource<QueryType, ResponseType, ResultType>(private val coroutineScope: CoroutineScope) {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.w("CoroutineExHan", "$javaClass -> Caught: $exception")
    }

    private val channel by lazy {
        coroutineScope.broadcast<Resource<ResultType>> {
            // keep the channel alive until the scope is not active
            while (isActive) delay(100)
            channel.close()
        }
    }

    private var lastData: ResultType? = null

    private var runningJob: Job? = null
    private val isInLoading = AtomicBoolean(false)
    private var isLoadMore = false

    protected val isLoadMoreInvalidation
        get() = isLoadMore

    fun invalidate(query: QueryType, loadMore: Boolean = false) {
        if (loadMore && isInLoading.get()) return

        isLoadMore = loadMore
        if (loadMore) {
            setValue(Resource.loading(lastData))
        } else {
            stopRunning()
            setValue(Resource.loading(null))
        }
        runningJob = coroutineScope.launch(Dispatchers.IO + exceptionHandler) {
            isInLoading.set(true)
            val dbResult = loadFromDb(query)
            if (shouldFetch(query, dbResult)) {
                fetchFromNetwork(query, dbResult)
            } else {
                setValue(Resource.success(dbResult))
                isInLoading.set(false)
            }
        }
    }

    private fun stopRunning() {
        if (runningJob?.isActive == true) {
            runningJob?.cancel()
        }
        isInLoading.set(false)
    }

    private fun setValue(newValue: Resource<ResultType>) {
        lastData = newValue.data
        coroutineScope.launch(exceptionHandler) {
            channel.send(newValue)
        }
    }

    private suspend fun fetchFromNetwork(query: QueryType, dbResult: ResultType?) {
        setValue(Resource.loading(dbResult))
        createCall(query).run {
            when (status) {
                Status.SUCCESS -> {
                    saveCallResult(query, processResponse(query, this))
                    setValue(Resource.success(loadFromDb(query)))
                    isInLoading.set(false)
                }
                Status.EMPTY -> {
                    setValue(Resource.success(loadFromDb(query)))
                    isInLoading.set(false)
                }
                Status.ERROR -> {
                    onFetchFailed(query)
                    setValue(Resource.error(error, dbResult))
                    isInLoading.set(false)
                }
                Status.LOADING -> {
                }
            }
        }
    }

    fun asFlow() = channel.asFlow()

    protected open fun onFetchFailed(query: QueryType) {}

    protected open suspend fun processResponse(
        query: QueryType,
        apiResponse: Resource<ResponseType>
    ) = apiResponse.data

    protected abstract suspend fun saveCallResult(query: QueryType, apiResponse: ResponseType?)

    protected abstract suspend fun loadFromDb(query: QueryType): ResultType?

    protected abstract suspend fun createCall(query: QueryType): Resource<ResponseType>

    protected abstract suspend fun shouldFetch(query: QueryType, dbResult: ResultType?): Boolean

}
