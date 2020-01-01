package com.aminography.foursquareapp.domain.model

/**
 * A data holder class which contains the fields that we need to show a venue item in UI.
 *
 * @author aminography
 */
data class VenueItemModel(
    val venueId: String,
    val name: String,
    val address: String?,
    val distance: String,
    val categoryIcon: String,
    val verified: Boolean
)