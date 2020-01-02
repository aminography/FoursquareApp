package com.aminography.foursquareapp.data.base

import com.aminography.foursquareapp.data.base.Status.*

/**
 * A generic class that holds a value with its loading status.
 *
 * @author aminography
 */
data class Resource<T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {

    companion object {

        /**
         * Creates a [Resource] instance with the status of the [Status.SUCCESS]
         */
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        /**
         * Creates a [Resource] instance with the status of the [Status.EMPTY]
         */
        fun <T> empty(data: T? = null): Resource<T> {
            return Resource(EMPTY, data, null)
        }

        /**
         * Creates a [Resource] instance with the status of the [Status.ERROR]
         */
        fun <T> error(error: Throwable?, data: T? = null): Resource<T> {
            return Resource(ERROR, data, error)
        }

        /**
         * Creates a [Resource] instance with the status of the [Status.LOADING]
         */
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }

    }
}

/**
 * Maps the data part of the [Resource] from one type to another.
 *
 * @param transform the transformation operation
 * @return a new instance of [Resource] with the transformed data preserving the [Status]
 */
inline fun <T, R> Resource<T>.mapDataNotNull(crossinline transform: (T) -> R): Resource<R> {
    return this.data?.let {
        Resource(status, transform(it), error)
    } ?: Resource(status, null as? R?, error)
}
