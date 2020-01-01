package com.aminography.foursquareapp.presentation.viewmodel

import androidx.lifecycle.*
import com.aminography.foursquareapp.domain.LoadVenueDetails
import com.aminography.foursquareapp.presentation.viewmodel.base.AbsentLiveData

/**
 * A [ViewModel] subclass which provides data from the repository to the view for the venue details.
 *
 * @author aminography
 */
class VenueDetailsViewModel(
    private val useCase: LoadVenueDetails
) : ViewModel() {

    private val queryLiveData = MutableLiveData<String?>()

    init {
        queryLiveData.value = null
    }

    /**
     * The [LiveData] instance that exposes data by observing it
     */
    val venueDetails = queryLiveData.switchMap { query ->
        query?.run {
            useCase.execute(viewModelScope, query).asLiveData()
        } ?: AbsentLiveData.create()
    }

    /**
     * Tries to search for the details of the venue corresponding to the venueId
     *
     * @param venueId the id of the venue to be retrieved its details
     */
    fun explore(venueId: String) {
        queryLiveData.postValue(venueId)
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