package com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue

import com.aminography.foursquareapp.data.datasource.remote.webservice.response.base.BaseResponseModel
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.common.Category
import com.google.gson.annotations.SerializedName

/**
 * A class that models the response of the venue recommendations
 *
 * @author aminography
 */
class VenueRecommendationsResponseModel(
    @SerializedName("response") val response: Response
) : BaseResponseModel() {

    class Response(
        @SerializedName("totalResults") val totalResults: Int,
        @SerializedName("groups") val groups: List<Group>
    )

    class Group(
        @SerializedName("name") val name: String,
        @SerializedName("items") val items: List<Item>
    )

    class Item(
        @SerializedName("venue") val venue: Venue
    )

    class Venue(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("location") val location: Location,
        @SerializedName("categories") val categories: List<Category>,
        @SerializedName("verified") val verified: Boolean
    )

    class Location(
        @SerializedName("address") val address: String?,
        @SerializedName("distance") val distance: Int
    )

}