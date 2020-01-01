package com.aminography.foursquareapp.presentation.viewmodel.base

import androidx.lifecycle.LiveData
import com.aminography.foursquareapp.data.base.Resource

/**
 * A [LiveData] class that has `null` value.
 *
 * @author aminography
 */
class AbsentLiveData<T : Any?> private constructor(
    resource: Resource<T>
) : LiveData<Resource<T>>() {

    init {
        // use post instead of set since this can be created on any thread
        postValue(resource)
    }

    companion object {

        fun <T> create(): LiveData<Resource<T>> {
            return AbsentLiveData(Resource.empty())
        }
    }
}
