package com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue

import com.aminography.foursquareapp.data.datasource.remote.webservice.response.base.BaseResponseModel
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.common.Category
import com.google.gson.annotations.SerializedName

/**
 * A class that models the response of the venue details
 *
 * @author aminography
 */
class VenueDetailsResponseModel(
    @SerializedName("response") val response: Response
) : BaseResponseModel() {

    class Response(
        @SerializedName("venue") val venue: Venue
    )

    class Venue(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("contact") val contact: Contact,
        @SerializedName("location") val location: Location,
        @SerializedName("categories") val categories: List<Category>,
        @SerializedName("verified") val verified: Boolean,
        @SerializedName("url") val url: String?,
        @SerializedName("price") val price: Price?,
        @SerializedName("likes") val likes: Likes,
        @SerializedName("rating") val rating: Double,
        @SerializedName("ratingColor") val ratingColor: String?,
        @SerializedName("ratingSignals") val ratingSignals: Int,
        @SerializedName("tips") val tips: Tips,
        @SerializedName("popular") val popular: Popular?,
        @SerializedName("bestPhoto") val bestPhoto: BestPhoto?
    )

    class Contact(
        @SerializedName("phone") val phone: String?,
        @SerializedName("formattedPhone") val formattedPhone: String?,
        @SerializedName("instagram") val instagram: String?
    )

    class Location(
        @SerializedName("address") val address: String?,
        @SerializedName("lat") val latitude: Double,
        @SerializedName("lng") val longitude: Double
    )

    class Price(
        @SerializedName("tier") val tier: Int,
        @SerializedName("message") val message: String,
        @SerializedName("currency") val currency: String
    )

    class Likes(
        @SerializedName("count") val count: Int
    )

    class User(
        @SerializedName("id") val id: String,
        @SerializedName("firstName") val firstName: String?,
        @SerializedName("lastName") val lastName: String?,
        @SerializedName("photo") val photo: UserPhoto
    ) {
        fun photoUrl() = "${photo.prefix}$id${photo.suffix}"
    }

    class UserPhoto(
        @SerializedName("prefix") val prefix: String,
        @SerializedName("suffix") val suffix: String
    )

    class Tips(
        @SerializedName("count") val count: Int,
        @SerializedName("groups") val groups: List<TipGroup>
    )

    class TipGroup(
        @SerializedName("count") val count: Int,
        @SerializedName("items") val items: List<TipItem>
    )

    class TipItem(
        @SerializedName("id") val id: String,
        @SerializedName("createdAt") val createdAt: Int,
        @SerializedName("text") val text: String,
        @SerializedName("likes") val likes: Likes,
        @SerializedName("agreeCount") val agreeCount: Int,
        @SerializedName("disagreeCount") val disagreeCount: Int,
        @SerializedName("user") val user: User
    )

    class BestPhoto(
        @SerializedName("id") val id: String,
        @SerializedName("createdAt") val createdAt: Int,
        @SerializedName("prefix") val prefix: String,
        @SerializedName("suffix") val suffix: String,
        @SerializedName("width") val width: Int,
        @SerializedName("height") val height: Int
    ) {
        fun url(size: Int = 640) = "$prefix$size$suffix"
    }

    class Popular(
        @SerializedName("isOpen") val isOpen: Boolean,
        @SerializedName("isLocalHoliday") val isLocalHoliday: Boolean,
        @SerializedName("timeframes") val timeframes: List<TimeFrames>
    )

    class TimeFrames(
        @SerializedName("days") val days: String,
        @SerializedName("includesToday") val includesToday: Boolean,
        @SerializedName("open") val open: List<Open>,
        @SerializedName("segments") val segments: List<String>
    )

    class Open(
        @SerializedName("renderedTime") val renderedTime: String
    )

}
