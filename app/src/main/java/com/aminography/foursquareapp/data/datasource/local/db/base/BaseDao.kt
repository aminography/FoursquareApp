package com.aminography.foursquareapp.data.datasource.local.db.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Basic functionalities of the DAO objects.
 *
 * @author aminography
 */
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    @Delete
    suspend fun delete(entity: T)

    @Update
    suspend fun update(entity: T)

}