package com.aminography.foursquareapp.presentation.viewmodel

import androidx.lifecycle.*
import com.aminography.foursquareapp.domain.LoadVenueRecommendations
import com.aminography.foursquareapp.presentation.viewmodel.base.AbsentLiveData
import com.google.android.gms.maps.model.LatLng

/**
 * A [ViewModel] subclass which provides data from the repository to the view for the venue recommendations.
 *
 * @author aminography
 */
class VenueRecommendationsViewModel(
    private val useCase: LoadVenueRecommendations
) : ViewModel() {

    private val queryLiveData = MutableLiveData<LatLng?>()

    init {
        queryLiveData.value = null
    }

    /**
     * The [LiveData] instance that exposes data by observing it
     */
    @Suppress("EXPERIMENTAL_API_USAGE")
    val venueRecommendations = queryLiveData.switchMap { query ->
        query?.run {
            useCase.execute(viewModelScope, query).asLiveData()
        } ?: AbsentLiveData.create()
    }

    /**
     * Tries to search for the venues around the input location
     *
     * @param location the location of the user to get venue recommendations around it
     */
    fun explore(location: LatLng) {
        queryLiveData.postValue(location)
    }

    /**
     * Makes a load more operation which makes the repository to provide more data.
     */
    fun loadMore() {
        useCase.loadMore(viewModelScope)
    }

    /**
     * Makes a retry operation
     */
    fun retry() {
        queryLiveData.value.let {
            queryLiveData.postValue(it)
        }
    }

}