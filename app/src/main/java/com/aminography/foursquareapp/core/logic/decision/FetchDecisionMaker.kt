package com.aminography.foursquareapp.core.logic.decision

import com.aminography.foursquareapp.data.datasource.local.LocalDataSource
import com.aminography.foursquareapp.core.logic.DISTANCE_THRESHOLD_METER
import com.aminography.foursquareapp.core.logic.INTERVAL_THRESHOLD_MINUTE
import com.aminography.foursquareapp.core.logic.decision.policy.DistancePolicy
import com.aminography.foursquareapp.core.logic.decision.policy.IntervalPolicy
import com.aminography.foursquareapp.core.logic.network.NetworkChecker
import com.google.android.gms.maps.model.LatLng

/**
 * This class decides when to fetch data from the webservice based on a list of decision policies.
 *
 * @author aminography
 */
class FetchDecisionMaker internal constructor(
    private val localDataSource: LocalDataSource,
    private val networkChecker: NetworkChecker
) {

    private val policies = arrayListOf(
        IntervalPolicy(INTERVAL_THRESHOLD_MINUTE),
        DistancePolicy(DISTANCE_THRESHOLD_METER)
    )

    /**
     * It checks the list of decision policies by passing the location the them.
     *
     * @param location current detected location of the user
     * @return true true if at least one of the policies is confirmed.
     */
    suspend fun shouldFetch(location: LatLng): Boolean {
        return if (networkChecker.isAvailable()) {
            // When this functions is calling, there exists some data in db,
            // so we can assess last known location.
            localDataSource.loadLastKnownLocation()?.let { lastKnown ->
                policies.any { it.shouldFetch(lastKnown, location) }
            } ?: true
        } else {
            false
        }
    }

}
