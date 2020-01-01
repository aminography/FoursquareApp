package com.aminography.foursquareapp.data.datasource.local.db.details

import androidx.room.*
import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity

/**
 * Entity definition of the `venueDetails` table which defines the table structure too.
 *
 * @author aminography
 */
@Entity(tableName = "venueDetails",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("locationId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["locationId"]),
        Index(value = ["locationId", "venueId"], unique = true)
    ])
data class VenueDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val locationId: Int,
    val venueId: String,
    val name: String,
    @Embedded(prefix = "contact_") val contact: Contact?,
    @Embedded(prefix = "location_") val location: Location,
    @Embedded(prefix = "category_") val category: Category,
    val verified: Boolean,
    val url: String?,
    @Embedded(prefix = "price_") val price: Price?,
    val likeCount: Int,
    @Embedded(prefix = "rating_") val rating: Rating?,
    @Embedded(prefix = "tip_") val lastTip: Tip?,
    val isOpen: Boolean?,
    @Embedded(prefix = "photo_") val bestPhoto: Photo?
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
        val tier: Int,
        val message: String,
        val currency: String
    )

    class Rating(
        val rating: Double,
        val ratingColor: String?,
        val ratingSignals: Int
    )

    class Tip(
        val createdAt: Int,
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
        val createdAt: Int,
        val width: Int,
        val height: Int,
        val url: String
    )

}