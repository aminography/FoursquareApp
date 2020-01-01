package com.aminography.foursquareapp.data.datasource.remote

import com.aminography.foursquareapp.data.datasource.remote.webservice.*
import com.aminography.foursquareapp.data.datasource.remote.webservice.base.CLIENT_ID
import com.aminography.foursquareapp.data.datasource.remote.webservice.base.CLIENT_SECRET
import com.aminography.foursquareapp.data.datasource.remote.webservice.base.VERSION
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueDetailsResponseModel
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueRecommendationsResponseModel
import com.aminography.foursquareapp.data.datasource.remote.webservice.base.withResponse
import com.aminography.foursquareapp.data.base.Resource

/**
 * The implementation webservice calls by delegating it to the Retrofit provided concrete object.
 *
 * @author aminography
 */
class RemoteDataSource internal constructor(private val iWebService: IWebService) {

    suspend fun venueRecommendations(
        location: String,
        radius: Int? = null,
        section: String? = null,
        query: String? = null,
        sortByDistance: Int? = null,
        openNow: Int? = null,
        offset: Int,
        limit: Int
    ): Resource<VenueRecommendationsResponseModel> =
        withResponse {
            iWebService.venueRecommendations(
                CLIENT_ID,
                CLIENT_SECRET,
                VERSION,
                location,
                radius,
                section,
                query,
                sortByDistance,
                openNow,
                offset,
                limit
            )
        }

    suspend fun venueDetails(
        venueId: String
    ): Resource<VenueDetailsResponseModel> =
        withResponse {
            iWebService.venueDetails(
                venueId,
                CLIENT_ID,
                CLIENT_SECRET,
                VERSION
            )
        }

}