package com.aminography.foursquareapp.data.datasource.remote.webservice.base

import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
 *
 * @author aminography
 */
sealed class ApiResponse<T> {

    companion object {

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(
                error.message ?: "unknown error"
            )
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body == null || response.code() == 204) {
                        ApiEmptyResponse()
                    } else {
                        ApiSuccessResponse(
                            body
                        )
                    }
                }
                response.code() == 500 -> ApiDownResponse(
                    response.message()
                )
                else -> {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) response.message() else msg
                    ApiErrorResponse(
                        errorMsg ?: "unknown error"
                    )
                }
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiDownResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

