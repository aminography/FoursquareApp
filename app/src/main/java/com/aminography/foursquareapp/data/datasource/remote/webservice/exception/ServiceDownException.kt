package com.aminography.foursquareapp.data.datasource.remote.webservice.exception

/**
 * @author aminography
 */
class ServiceDownException(message: String?) : WebServiceException(message ?: "Server is unreachable!")