package com.aminography.foursquareapp.data.datasource.remote.webservice.response

import com.google.gson.annotations.SerializedName

/**
 * A class that models the error response of a webservice call
 *
 * @author aminography
 */
class ErrorResponseModel(
    @SerializedName("meta") val meta: Meta? = null
) {
    class Meta(
        @SerializedName("code") val code: Int,
        @SerializedName("requestId") val requestId: String,
        @SerializedName("errorType") val errorType: String?,
        @SerializedName("errorDetail") val errorDetail: String?
    )
}