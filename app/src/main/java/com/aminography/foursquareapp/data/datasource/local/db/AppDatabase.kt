package com.aminography.foursquareapp.data.datasource.local.db

import androidx.annotation.VisibleForTesting
import androidx.room.Database
import com.aminography.foursquareapp.data.datasource.local.db.base.BaseDatabase
import com.aminography.foursquareapp.data.datasource.local.db.base.DatabaseInstanceBuilder
import com.aminography.foursquareapp.data.datasource.local.db.details.VenueDetailsDao
import com.aminography.foursquareapp.data.datasource.local.db.details.VenueDetailsEntity
import com.aminography.foursquareapp.data.datasource.local.db.location.LocationDao
import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity
import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueDao
import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueEntity

/**
 * A class which defines abstraction of the desired database class to be provide by Room.
 *
 * @author aminography
 */
@Database(
    entities = [
        LocationEntity::class,
        VenueEntity::class,
        VenueDetailsEntity::class
    ],
    version = 21
)
abstract class AppDatabase : BaseDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun venueDao(): VenueDao

    abstract fun venueDetailsDao(): VenueDetailsDao

    companion object : DatabaseInstanceBuilder()
}

@VisibleForTesting
val DATABASE_NAME = "app-db"
