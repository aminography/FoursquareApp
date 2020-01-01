package com.aminography.foursquareapp.domain.repository

import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.domain.model.VenueDetailsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IVenueDetailsRepository {

    fun loadVenueDetails(
        coroutineScope: CoroutineScope,
        venueId: String
    ): Flow<Resource<VenueDetailsModel>>

}