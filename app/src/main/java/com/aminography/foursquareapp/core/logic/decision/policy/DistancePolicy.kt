package com.aminography.foursquareapp.core.logic.decision.policy

import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity
import com.aminography.foursquareapp.presentation.ui.distanceTo
import com.google.android.gms.maps.model.LatLng

/**
 * A concrete implementation of the [BaseDecisionPolicy] with logic of the measuring distance.
 *
 * @author aminography
 */
internal class DistancePolicy(private val threshold: Int) :
    BaseDecisionPolicy {

    @Synchronized
    override fun shouldFetch(
        lastKnownLocationEntity: LocationEntity,
        currentLocation: LatLng
    ): Boolean {
        val lastKnownLocation = lastKnownLocationEntity.let {
            LatLng(it.latitude, it.longitude)
        }
        val distance = lastKnownLocation.distanceTo(currentLocation)
        if (distance > threshold) {
            return true
        }
        return false
    }

}