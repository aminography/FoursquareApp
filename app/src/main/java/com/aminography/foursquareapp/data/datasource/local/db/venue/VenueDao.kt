package com.aminography.foursquareapp.data.datasource.local.db.venue

import androidx.room.Dao
import androidx.room.Query
import com.aminography.foursquareapp.data.datasource.local.db.base.BaseDao
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the `venue` table.
 *
 * @author aminography
 */
@Dao
interface VenueDao : BaseDao<VenueEntity> {

    @Query("SELECT * FROM venue WHERE venueId = :venueId")
    suspend fun load(venueId: String): VenueEntity

    @Query("SELECT * FROM venue WHERE locationId = :locationId LIMIT :limit OFFSET :offset")
    suspend fun loadAllByLocation(locationId: Int, offset: Int, limit: Int): List<VenueEntity>

    @Query("SELECT COUNT(id) FROM venue")
    suspend fun count(): Int

//    @Query("SELECT * FROM venue")
//    fun loadAll(): LiveData<List<VenueEntity>>

    @Query("SELECT * FROM venue")
    fun loadAllFlow(): Flow<List<VenueEntity>>

//    @Query("SELECT * FROM venue WHERE locationId = :locationId")
//    fun loadAllByLocationDSF(
//        locationId: Int
//    ): DataSource.Factory<Int, VenueEntity>
//
//    @Query("SELECT * FROM venue")
//    fun loadAllSync(): List<VenueEntity>
//
//    @Query("SELECT COUNT(id) FROM venue")
//    fun countSync(): Int

}
