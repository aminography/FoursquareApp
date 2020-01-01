package com.aminography.foursquareapp.presentation.ui.recommendations.dataholder

import com.aminography.foursquareapp.domain.data.VenueItemData
import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter

/**
 * A data holder class which contains the fields that we need to show a venue item in UI.
 *
 * @author aminography
 */
data class VenueItemDataHolder(
    val data: VenueItemData
) : BaseAdapter.BaseDataHolder()