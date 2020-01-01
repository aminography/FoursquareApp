package com.aminography.foursquareapp.presentation.ui.details

/**
 * A data holder class which contains the fields that we need to show venue details in UI.
 *
 * @author aminography
 */
data class VenueDetailsDataHolder(
    val id: String,
    val name: String,
    val contact: Contact?,
    val location: Location,
    val category: Category,
    val verified: Boolean,
    val url: String?,
    val price: Price?,
    val likeCount: Int,
    val rating: Rating?,
    val lastTip: Tip?,
    val isOpen: Boolean?,
    val bestPhoto: Photo?
) {

    class Contact(
        val phone: String?,
        val formattedPhone: String?,
        val instagram: String?
    )

    class Location(
        val address: String?,
        val latitude: Double,
        val longitude: Double
    )

    class Category(
        val name: String,
        val icon: String
    )

    class Price(
        val tier: Int, // 1 - 4
        val message: String,
        val currency: String
    )

    class Rating(
        val rating: Double, // 0 - 10
        val ratingColor: String?,
        val ratingSignals: Int
    )

    class Tip(
        val createdAt: String,
        val text: String,
        val likeCount: Int,
        val agreeCount: Int,
        val disagreeCount: Int,
        val userId: String,
        val userFirstName: String?,
        val userLastName: String?,
        val userPhoto: String
    )

    class Photo(
        val id: String,
        val createdAt: String,
        val width: Int,
        val height: Int,
        val url: String
    )

}