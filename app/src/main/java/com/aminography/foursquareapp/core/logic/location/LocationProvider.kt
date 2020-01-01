package com.aminography.foursquareapp.core.logic.location

import com.google.android.gms.location.FusedLocationProviderClient

/**
 * A class which provides an instance of LiveData to observe the current location.
 *
 * @author aminography
 */
class LocationProvider private constructor(
    locationProviderClient: FusedLocationProviderClient
) {

    /**
     * Observable current location
     */
    val liveData = LocationLiveData.get(locationProviderClient)

    companion object {

        fun createInstance(locationProviderClient: FusedLocationProviderClient): LocationProvider {
            return LocationProvider(locationProviderClient)
        }
    }

}