package com.aminography.foursquareapp.data.datasource.local

import com.aminography.foursquareapp.data.datasource.local.db.details.VenueDetailsDao
import com.aminography.foursquareapp.data.datasource.local.db.details.VenueDetailsEntity
import com.aminography.foursquareapp.data.datasource.local.db.location.LocationDao
import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity
import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueDao
import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueEntity

class LocalDataSource internal constructor(
    private val locationDao: LocationDao,
    private val venueDao: VenueDao,
    private val venueDetailsDao: VenueDetailsDao
) {

    suspend fun insertLocation(entity: LocationEntity) =
        locationDao.insert(entity)

    suspend fun deleteLocation(entity: LocationEntity) =
        locationDao.delete(entity)

    suspend fun loadLastKnownLocation(): LocationEntity? =
        locationDao.loadLastKnown()

    // ---------------------------------------------------------------------------------------------

    suspend fun insertAllVenues(entities: List<VenueEntity>) =
        venueDao.insertAll(entities)

    suspend fun loadAllByLocation(locationId: Int, offset: Int, limit: Int): List<VenueEntity> =
        venueDao.loadAllByLocation(locationId, offset, limit)

    suspend fun venueCount(): Int =
        venueDao.count()

    // ---------------------------------------------------------------------------------------------

    suspend fun loadVenueDetails(venueId: String): VenueDetailsEntity =
        venueDetailsDao.load(venueId)

    suspend fun insertVenueDetails(entity: VenueDetailsEntity) =
        venueDetailsDao.insert(entity)

}