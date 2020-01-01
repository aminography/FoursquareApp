package com.aminography.foursquareapp.domain

import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.domain.base.BaseUseCase
import com.aminography.foursquareapp.domain.repository.IVenueRecommendationsRepository
import com.aminography.foursquareapp.presentation.ui.venues.dataholder.VenueItemDataHolder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class LoadVenueRecommendations internal constructor(
    private val repo: IVenueRecommendationsRepository
) : BaseUseCase<LatLng, Flow<Resource<List<VenueItemDataHolder>>>>() {

    override fun execute(
        coroutineScope: CoroutineScope,
        params: LatLng
    ): Flow<Resource<List<VenueItemDataHolder>>> {
        return repo.loadVenueRecommendations(coroutineScope, params)
    }

    fun loadMore(coroutineScope: CoroutineScope) {
        repo.loadMore(coroutineScope)
    }

}