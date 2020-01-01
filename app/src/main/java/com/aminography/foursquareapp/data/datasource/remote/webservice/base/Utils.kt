package com.aminography.foursquareapp.data.datasource.remote.webservice.base

import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.data.datasource.remote.webservice.IWebService
import com.aminography.foursquareapp.data.datasource.remote.webservice.exception.ServiceDownException
import com.aminography.foursquareapp.data.datasource.remote.webservice.exception.WebServiceException
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.ErrorResponseModel
import com.google.gson.Gson
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

/**
 * @author aminography
 */

/**
 * Converts response of the retrofit to a Resource object.
 *
 * @param <T> type of the response model
 * @param function lambda function which provides the webservice response
 * @return a Resource instance containing response model
 */
internal inline fun <T> withResponse(function: () -> Response<T>): Resource<T> {
    return try {
        when (val response =
            ApiResponse.create(
                function.invoke()
            )) {
            is ApiSuccessResponse -> Resource.success(response.body)
            is ApiEmptyResponse -> Resource.empty()
            is ApiDownResponse -> Resource.error(ServiceDownException(response.errorMessage))
            is ApiErrorResponse -> {
                val error = Gson().fromJson(
                    response.errorMessage,
                    ErrorResponseModel::class.java
                )
                Resource.error(WebServiceException(error.meta?.errorDetail))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.error(e)
    }
}