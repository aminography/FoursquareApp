package com.aminography.foursquareapp.domain.data

/**
 * A data holder class which contains the fields that we need to show a venue item in UI.
 *
 * @author aminography
 */
data class VenueItemData(
    val venueId: String,
    val name: String,
    val address: String?,
    val distance: String,
    val categoryIcon: String,
    val verified: Boolean
)