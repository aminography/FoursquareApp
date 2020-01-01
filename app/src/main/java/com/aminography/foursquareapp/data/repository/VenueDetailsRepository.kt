package com.aminography.foursquareapp.data.repository

import com.aminography.foursquareapp.data.base.NetworkBoundResource
import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.data.base.map
import com.aminography.foursquareapp.data.datasource.local.LocalDataSource
import com.aminography.foursquareapp.data.datasource.local.db.details.VenueDetailsEntity
import com.aminography.foursquareapp.data.datasource.remote.RemoteDataSource
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueDetailsResponseModel
import com.aminography.foursquareapp.domain.repository.IVenueDetailsRepository
import com.aminography.foursquareapp.domain.toVenueDetailsDataHolder
import com.aminography.foursquareapp.domain.toVenueDetailsEntity
import com.aminography.foursquareapp.presentation.ui.details.VenueDetailsDataHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * The repository class which provides venue details data for the ViewMode layer.
 *
 * @author aminography
 */
class VenueDetailsRepository internal constructor(
    localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource
) : IVenueDetailsRepository {

    private lateinit var coroutineScope: CoroutineScope

    private val resource: NetworkBoundResource<
            String, VenueDetailsResponseModel, VenueDetailsEntity> by lazy {

        object : NetworkBoundResource<
                String, VenueDetailsResponseModel, VenueDetailsEntity>(coroutineScope) {

            override suspend fun loadFromDb(query: String): VenueDetailsEntity? {
                return localDataSource.loadVenueDetails(query)
            }

            override suspend fun saveCallResult(
                query: String,
                apiResponse: VenueDetailsResponseModel?
            ) {
                localDataSource.loadLastKnownLocation()?.id?.let { locationId ->
                    apiResponse?.toVenueDetailsEntity(locationId)?.let {
                        localDataSource.insertVenueDetails(it)
                    }
                }
            }

            override suspend fun createCall(query: String): Resource<VenueDetailsResponseModel> {
                return remoteDataSource.venueDetails(query)
            }

            override suspend fun shouldFetch(
                query: String,
                dbResult: VenueDetailsEntity?
            ): Boolean {
                return dbResult == null
            }

        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val flow by lazy {
        resource.asFlow()
            .map { resource ->
                resource.map { entity ->
                    entity?.toVenueDetailsDataHolder()
                }
            }
            .flowOn(Dispatchers.Default)
    }

    /**
     * Creates an instance of [Flow] which provides venue details based on input venueId.
     *
     * @param coroutineScope scope that the executor coroutines will be launch on it
     * @param venueId the id of the venue to provide its details
     * @return an instance of [Flow] which holds a resource object of the desired data
     */
    override fun loadVenueDetails(
        coroutineScope: CoroutineScope,
        venueId: String
    ): Flow<Resource<VenueDetailsDataHolder>> {
        this.coroutineScope = coroutineScope
        return flow.also {
            resource.invalidate(venueId)
        }
    }

}