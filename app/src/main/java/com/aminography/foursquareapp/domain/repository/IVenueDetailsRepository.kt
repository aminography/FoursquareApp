package com.aminography.foursquareapp.domain.repository

import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.presentation.ui.details.VenueDetailsDataHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IVenueDetailsRepository {

    fun loadVenueDetails(
        coroutineScope: CoroutineScope,
        venueId: String
    ): Flow<Resource<VenueDetailsDataHolder>>

}