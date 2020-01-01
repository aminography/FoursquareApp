package com.aminography.foursquareapp.data.datasource.local.db.venue

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity

/**
 * Entity definition of the `venue` table which defines the table structure too.
 *
 * @author aminography
 */
@Entity(
    tableName = "venue",
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
    ]
)
data class VenueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val locationId: Int,
    val venueId: String,
    val name: String,
    val address: String?,
    val distance: Int,
    val categoryIcon: String,
    val verified: Boolean
)
