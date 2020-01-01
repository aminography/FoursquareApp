package com.aminography.foursquareapp.domain.repository

import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.presentation.ui.recommendations.dataholder.VenueItemDataHolder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IVenueRecommendationsRepository {

    fun loadVenueRecommendations(
        coroutineScope: CoroutineScope,
        location: LatLng
    ): Flow<Resource<List<VenueItemDataHolder>>>

    fun loadMore(coroutineScope: CoroutineScope)

}