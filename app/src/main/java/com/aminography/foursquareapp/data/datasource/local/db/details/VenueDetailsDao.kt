package com.aminography.foursquareapp.data.datasource.local.db.details

import androidx.room.Dao
import androidx.room.Query
import com.aminography.foursquareapp.data.datasource.local.db.base.BaseDao

/**
 * Data Access Object for the `venueDetails` table.
 *
 * @author aminography
 */
@Dao
interface VenueDetailsDao : BaseDao<VenueDetailsEntity> {

    @Query("SELECT * FROM venueDetails WHERE venueId = :venueId")
    suspend fun load(venueId: String): VenueDetailsEntity

}
