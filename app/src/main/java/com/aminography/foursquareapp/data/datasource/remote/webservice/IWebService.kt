package com.aminography.foursquareapp.data.datasource.remote.webservice

import com.aminography.foursquareapp.data.datasource.remote.webservice.base.CONTENT_TYPE
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueDetailsResponseModel
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueRecommendationsResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Declares the interface of the webservice object which is provided by the Retrofit.
 *
 * @author aminography
 */
interface IWebService {

    @Headers(CONTENT_TYPE)
    @GET("v2/venues/explore")
    suspend fun venueRecommendations(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") version: String,
        @Query("ll") location: String,
        @Query("radius") radius: Int?,
        @Query("section") section: String?,
        @Query("query") query: String?,
        @Query("sortByDistance") sortByDistance: Int?,
        @Query("openNow") openNow: Int?,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<VenueRecommendationsResponseModel>

    @Headers(CONTENT_TYPE)
    @GET("v2/venues/{venue_id}")
    suspend fun venueDetails(
        @Path("venue_id") venueId: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") version: String
    ): Response<VenueDetailsResponseModel>

}
