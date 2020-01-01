package com.aminography.foursquareapp.core.logic.network

import android.content.Context
import com.aminography.foursquareapp.core.tools.NetworkUtils

/**
 * A class which provides the check of network availability.
 *
 * @author aminography
 */
class NetworkChecker internal constructor(private val context: Context) {

    /**
     * Checks the network availability
     *
     * @return true if the network is availability
     */
    fun isAvailable(): Boolean = NetworkUtils.isNetworkAvailable(context)
}