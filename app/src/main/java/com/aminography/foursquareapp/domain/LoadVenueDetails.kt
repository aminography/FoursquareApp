package com.aminography.foursquareapp.domain

import com.aminography.foursquareapp.data.base.Resource
import com.aminography.foursquareapp.domain.base.BaseUseCase
import com.aminography.foursquareapp.domain.model.VenueDetailsModel
import com.aminography.foursquareapp.domain.repository.IVenueDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class LoadVenueDetails internal constructor(
    private val repo: IVenueDetailsRepository
) : BaseUseCase<String, Flow<Resource<VenueDetailsModel>>>() {

    override fun execute(
        coroutineScope: CoroutineScope,
        params: String
    ): Flow<Resource<VenueDetailsModel>> {
        return repo.loadVenueDetails(coroutineScope, params)
    }

}