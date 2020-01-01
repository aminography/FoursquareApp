package com.aminography.foursquareapp.core.logic.location

import com.google.android.gms.maps.model.LatLng

/**
 * A class to wrap location responses
 *
 * @author aminography
 */
sealed class LocationResponse {

    companion object {

        fun create(error: Throwable): ErrorResponse {
            return ErrorResponse(error.message ?: "unknown error")
        }

        fun create(location: LatLng): SuccessResponse {
            return SuccessResponse(location)
        }

    }

    data class ErrorResponse(val errorMessage: String) : LocationResponse()

    data class SuccessResponse(val location: LatLng) : LocationResponse()
}