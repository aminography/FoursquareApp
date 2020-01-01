package com.aminography.foursquareapp.core.logic.decision.policy

import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.TimeUnit

/**
 * A concrete implementation of the [BaseDecisionPolicy] with logic of the measuring time intervals.
 *
 * @author aminography
 */
internal class IntervalPolicy(private val threshold: Int) :
    BaseDecisionPolicy {

    @Synchronized
    override fun shouldFetch(
        lastKnownLocationEntity: LocationEntity,
        currentLocation: LatLng
    ): Boolean {
        val interval = TimeUnit.MINUTES.toMillis(threshold.toLong())
        if (now() - lastKnownLocationEntity.timestamp > interval) {
            return true
        }
        return false
    }

    private fun now() = System.currentTimeMillis()

}