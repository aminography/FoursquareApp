package com.aminography.foursquareapp.core.logic.decision.policy

import com.aminography.foursquareapp.data.datasource.local.db.location.LocationEntity
import com.google.android.gms.maps.model.LatLng

/**
 * Base interface of a decision policy.
 *
 * @author aminography
 */
internal interface BaseDecisionPolicy {

    fun shouldFetch(
        lastKnownLocationEntity: LocationEntity,
        currentLocation: LatLng
    ): Boolean

}