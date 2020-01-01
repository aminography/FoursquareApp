package com.aminography.foursquareapp.presentation.ui.venues.dataholder

import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter

/**
 * A data holder class which contains the fields that we need to show a venue item in UI.
 *
 * @author aminography
 */
data class VenueItemDataHolder(
    val venueId: String,
    val name: String,
    val address: String?,
    val distance: String,
    val categoryIcon: String,
    val verified: Boolean
) : BaseAdapter.BaseDataHolder()