package com.aminography.foursquareapp.data.datasource.local.db.location

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity definition of the `location` table which defines the table structure too.
 *
 * @author aminography
 */
@Entity(
    tableName = "location"
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val latitude: Double,
    val longitude: Double,
    val totalResults: Int,
    val timestamp: Long
)
