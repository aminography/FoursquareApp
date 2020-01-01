package com.aminography.foursquareapp.data.repository

import com.aminography.foursquareapp.core.logic.decision.FetchDecisionMaker
import com.aminography.foursquareapp.core.logic.network.NetworkChecker
import com.aminography.foursquareapp.data.base.NetworkBoundResource
import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.data.base.mapData
import com.aminography.foursquareapp.data.datasource.local.LocalDataSource
import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity
import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueEntity
import com.aminography.foursquareapp.data.datasource.remote.RemoteDataSource
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueRecommendationsResponseModel
import com.aminography.foursquareapp.domain.data.VenueItemData
import com.aminography.foursquareapp.domain.repository.IVenueRecommendationsRepository
import com.aminography.foursquareapp.domain.toVenueEntity
import com.aminography.foursquareapp.domain.toVenueItemData
import com.aminography.foursquareapp.presentation.ui.concat
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The repository class which provides venue recommendations data for the ViewMode layer.
 *
 * @author aminography
 */
class VenueRecommendationsRepository internal constructor(
    localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource,
    fetchDecisionMaker: FetchDecisionMaker,
    networkChecker: NetworkChecker
) : IVenueRecommendationsRepository {

    private var offset = 0
    private var loadMoreFinished = false
    private var needMoreItemsFromNetwork = false

    private var query: LatLng? = null
    private val dbResults: CopyOnWriteArrayList<VenueEntity> = CopyOnWriteArrayList()

    private lateinit var coroutineScope: CoroutineScope

    private val resource: NetworkBoundResource<
            LatLng, VenueRecommendationsResponseModel, List<VenueEntity>> by lazy {

        object : NetworkBoundResource<
                LatLng, VenueRecommendationsResponseModel, List<VenueEntity>>(coroutineScope) {

            override suspend fun loadFromDb(query: LatLng): List<VenueEntity>? {
                needMoreItemsFromNetwork = false
                localDataSource.loadLastKnownLocation()?.let { entity ->
                    val totalResults = entity.totalResults
                    val locationId = entity.id!!

                    if (dbResults.size < totalResults) {
                        val list = localDataSource.loadAllByLocation(locationId, offset, PAGE_SIZE)
                        dbResults.addAll(list)
                        offset += list.size
                        if (dbResults.size >= totalResults) {
                            loadMoreFinished = true
                        } else if (dbResults.size + PAGE_SIZE > localDataSource.venueCount()) {
                            needMoreItemsFromNetwork = true
                        }
                    }
                }
                return dbResults
            }

            override suspend fun saveCallResult(
                query: LatLng,
                apiResponse: VenueRecommendationsResponseModel?
            ) {

                suspend fun saveApiResponse(): Int {
                    var responseSize = 0
                    localDataSource.loadLastKnownLocation()?.id?.let { locationId ->
                        apiResponse?.response?.groups?.get(0)?.items?.map {
                            it.toVenueEntity(locationId)
                        }?.let {
                            responseSize = it.size
                            localDataSource.insertAllVenues(it)
                        }
                    }
                    return responseSize
                }

                suspend fun saveNewLocation() {
                    localDataSource.insertLocation(
                        LocationEntity(
                            null,
                            query.latitude,
                            query.longitude,
                            apiResponse?.response?.totalResults ?: 0,
                            System.currentTimeMillis()
                        )
                    )
                }

                if (isLoadMoreInvalidation && needMoreItemsFromNetwork) {
                    val responseCount = saveApiResponse()
                    loadMoreFinished = (responseCount < PAGE_SIZE)
                } else {
                    val previousKnownLocation = localDataSource.loadLastKnownLocation()
                    saveNewLocation()
                    // Deleting previous location and belonging venues (including details) gracefully.
                    previousKnownLocation?.let { localDataSource.deleteLocation(it) }

                    saveApiResponse()
                }
            }

            override suspend fun createCall(query: LatLng): Resource<VenueRecommendationsResponseModel> {
                return remoteDataSource.venueRecommendations(
                    location = query.concat(),
                    sortByDistance = 1,
                    offset = offset,
                    limit = PAGE_SIZE
                )
            }

            override suspend fun shouldFetch(query: LatLng, dbResult: List<VenueEntity>?): Boolean {
                val decision = fetchDecisionMaker.shouldFetch(query)
                return if (decision) {
                    clearLoadState()
                    true
                } else {
                    if (isLoadMoreInvalidation) {
                        needMoreItemsFromNetwork && networkChecker.isAvailable()
                    } else {
                        dbResult.isNullOrEmpty()
                    }
                }
            }
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val flow by lazy {
        resource.asFlow()
            .map { resource ->
                resource.mapData { list ->
                    list?.map { entity ->
                        entity.toVenueItemData()
                    }
                }
            }
            .flowOn(Dispatchers.Default)
    }

    private fun clearLoadState() {
        dbResults.clear()
        offset = 0
        loadMoreFinished = false
        needMoreItemsFromNetwork = false
    }

    /**
     * Creates an instance of [Flow] which provides venue recommendations based on input location.
     *
     * @param coroutineScope scope that the executor coroutines will be launch on it
     * @param location the location of the user to get venue recommendations around it
     * @return an instance of [Flow] which holds a resource object of the desired data
     */
    override fun loadVenueRecommendations(
        coroutineScope: CoroutineScope,
        location: LatLng
    ): Flow<Resource<List<VenueItemData>>> {
        this.coroutineScope = coroutineScope
        return flow.also {
            query = location
            clearLoadState()
            resource.invalidate(location)
        }
    }

    /**
     * Makes a load more operation which makes the repository to provide more data.
     *
     * @param coroutineScope scope that the executor coroutines will be launch on it
     */
    override fun loadMore(coroutineScope: CoroutineScope) {
        if (!loadMoreFinished) {
            query?.let {
                this.coroutineScope = coroutineScope
                resource.invalidate(it, true)
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 30
    }

}
