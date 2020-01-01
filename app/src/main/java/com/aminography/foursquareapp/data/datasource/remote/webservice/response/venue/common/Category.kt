package com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.common

import com.google.gson.annotations.SerializedName

/**
 * A class that models a Category
 *
 * @author aminography
 */
class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: Icon
) {

    class Icon(
        @SerializedName("prefix") val prefix: String,
        @SerializedName("suffix") val suffix: String
    ) {
        fun url(size: Int = 88) = "$prefix$size$suffix"
    }

}

