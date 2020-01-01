package com.aminography.foursquareapp.data.datasource.local.db.location

import androidx.room.Dao
import androidx.room.Query
import com.aminography.foursquareapp.data.datasource.local.db.base.BaseDao

/**
 * Data Access Object for the `location` table.
 *
 * @author aminography
 */
@Dao
interface LocationDao : BaseDao<LocationEntity> {

    @Query("SELECT * FROM location WHERE id = :id")
    suspend fun load(id: String): LocationEntity?

    @Query("SELECT * FROM location ORDER BY id DESC LIMIT 1")
    suspend fun loadLastKnown(): LocationEntity?

    @Query("SELECT * FROM location ORDER BY id DESC LIMIT 1")
    fun loadLastKnownSync(): LocationEntity?

    @Query("SELECT * FROM location ORDER BY id DESC")
    fun loadAllSync(): List<LocationEntity>?

}
