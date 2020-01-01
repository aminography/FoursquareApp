package com.aminography.foursquareapp.core.logic.location

import android.location.Location
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

/**
 * An implementation of LiveData with can be observed to know the current location
 *
 * @author aminography
 */
class LocationLiveData private constructor(
    private val locationProviderClient: FusedLocationProviderClient
) : LiveData<LocationResponse>() {

    override fun onActive() {
        locationProviderClient.lastLocation?.apply {
            onSuccessListener?.let { addOnSuccessListener(it) }
            onFailureListener?.let { addOnFailureListener(it) }
        }
    }

    override fun onInactive() {
        onSuccessListener = null
        onFailureListener = null
    }

    private var onSuccessListener: OnSuccessListener<Location>? = OnSuccessListener {
        it?.apply {
            postValue(LocationResponse.create(LatLng(latitude, longitude)))
        }
    }

    private var onFailureListener: OnFailureListener? = OnFailureListener {
        postValue(LocationResponse.create(it))
    }

    internal companion object {

        @MainThread
        internal fun get(locationProviderClient: FusedLocationProviderClient): LocationLiveData {
            return LocationLiveData(locationProviderClient)
        }
    }

}