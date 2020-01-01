package com.aminography.foursquareapp.core.tools

import java.util.*

/**
 * A class contains functions to raw make values human readable
 *
 * @author aminography
 */

/**
 * A functions that makes distance values human readable
 *
 * @param distance in meter
 * @return formatted distance text
 */
fun formatDistance(distance: Int): String {
    return when (distance) {
        in 0..1000 -> String.format(Locale.getDefault(), "%d m", distance)
        else -> String.format(Locale.getDefault(), "%.1f km", distance.toDouble() / 1000)
    }
}
