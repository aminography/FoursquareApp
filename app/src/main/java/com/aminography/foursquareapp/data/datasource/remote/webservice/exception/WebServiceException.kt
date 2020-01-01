package com.aminography.foursquareapp.data.datasource.remote.webservice.exception

/**
 * @author aminography
 */
open class WebServiceException(message: String?) : Throwable(message ?: "Service is unreachable!")