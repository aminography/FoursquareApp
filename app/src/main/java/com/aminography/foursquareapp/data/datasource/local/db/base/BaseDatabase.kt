package com.aminography.foursquareapp.data.datasource.local.db.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.RoomDatabase
import com.aminography.foursquareapp.data.datasource.local.db.DATABASE_NAME

/**
 * @author aminography
 */
abstract class BaseDatabase : RoomDatabase() {

    val isDatabaseCreated = MutableLiveData<Boolean>()

    internal fun setDatabaseCreated() = isDatabaseCreated.postValue(true)

    /**
     * Check whether the database already exists.
     */
    internal fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

}