package com.aminography.foursquareapp.data.datasource.remote.webservice.response.base

import com.google.gson.annotations.SerializedName

/**
 * Base class of the webservice response model containing common attributes
 *
 * @author aminography
 */
abstract class BaseResponseModel(
    @SerializedName("meta") val meta: Meta? = null
) {
    class Meta(
        @SerializedName("code") val code: Int,
        @SerializedName("requestId") val requestId: String
    )
}
