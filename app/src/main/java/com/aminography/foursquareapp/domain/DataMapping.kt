package com.aminography.foursquareapp.domain

import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueDetailsResponseModel
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueRecommendationsResponseModel
import com.aminography.foursquareapp.data.datasource.local.db.details.VenueDetailsEntity
import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueEntity
import com.aminography.foursquareapp.core.tools.formatDistance
import com.aminography.foursquareapp.presentation.ui.details.VenueDetailsDataHolder
import com.aminography.foursquareapp.presentation.ui.venues.dataholder.VenueItemDataHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * A class contains extension functions to map data objects between layers
 *
 * @author aminography
 */

fun VenueRecommendationsResponseModel.Item.toVenueEntity(locationId: Int) =
    VenueEntity(
        null,
        locationId,
        venue.id,
        venue.name,
        venue.location.address,
        venue.location.distance,
        venue.categories[0].icon.url(),
        venue.verified
    )

fun VenueEntity.toVenueItemDataHolder() =
    VenueItemDataHolder(
        venueId,
        name,
        address,
        formatDistance(distance),
        categoryIcon,
        verified
    )

fun VenueDetailsResponseModel.toVenueDetailsEntity(locationId: Int) =
    response.venue.run {
        VenueDetailsEntity(
            null,
            locationId,
            id,
            name,
            VenueDetailsEntity.Contact(
                contact.phone,
                contact.formattedPhone,
                contact.instagram
            ),
            VenueDetailsEntity.Location(
                location.address,
                location.latitude,
                location.longitude
            ),
            VenueDetailsEntity.Category(
                categories[0].name,
                categories[0].icon.url()
            ),
            verified,
            url,
            if (price != null) {
                VenueDetailsEntity.Price(
                    price.tier,
                    price.message,
                    price.currency
                )
            } else {
                null
            }
            ,
            likes.count,
            VenueDetailsEntity.Rating(
                rating,
                ratingColor,
                ratingSignals
            ),
            if (tips.groups.isNotEmpty() && tips.groups[0].items.isNotEmpty()) {
                VenueDetailsEntity.Tip(
                    tips.groups[0].items[0].createdAt,
                    tips.groups[0].items[0].text,
                    tips.groups[0].items[0].likes.count,
                    tips.groups[0].items[0].agreeCount,
                    tips.groups[0].items[0].disagreeCount,
                    tips.groups[0].items[0].user.id,
                    tips.groups[0].items[0].user.firstName,
                    tips.groups[0].items[0].user.lastName,
                    tips.groups[0].items[0].user.photoUrl()
                )
            } else {
                null
            }
            ,
            popular?.isOpen,
            if (bestPhoto != null) {
                VenueDetailsEntity.Photo(
                    bestPhoto.id,
                    bestPhoto.createdAt,
                    bestPhoto.width,
                    bestPhoto.height,
                    bestPhoto.url()
                )
            } else {
                null
            }
        )
    }

fun VenueDetailsEntity.toVenueDetailsDataHolder() =
    VenueDetailsDataHolder(
        venueId,
        name,
        VenueDetailsDataHolder.Contact(
            contact?.phone,
            contact?.formattedPhone,
            contact?.instagram
        ),
        VenueDetailsDataHolder.Location(
            location.address,
            location.latitude,
            location.longitude
        ),
        VenueDetailsDataHolder.Category(
            category.name,
            category.icon
        ),
        verified,
        url,
        if (price != null) {
            VenueDetailsDataHolder.Price(
                price.tier,
                price.message,
                price.currency
            )
        } else {
            null
        }
        ,
        likeCount,
        if (rating != null) {
            VenueDetailsDataHolder.Rating(
                rating.rating,
                rating.ratingColor,
                rating.ratingSignals
            )
        } else {
            null
        },
        if (lastTip != null) {
            VenueDetailsDataHolder.Tip(
                SimpleDateFormat(
                    "MMMM d, yyyy",
                    Locale.ENGLISH
                ).format(Date(lastTip.createdAt.toLong() * 1000)),
                lastTip.text,
                lastTip.likeCount,
                lastTip.agreeCount,
                lastTip.disagreeCount,
                lastTip.userId,
                lastTip.userFirstName,
                lastTip.userLastName,
                lastTip.userPhoto
            )
        } else {
            null
        }
        ,
        isOpen,
        if (bestPhoto != null) {
            VenueDetailsDataHolder.Photo(
                bestPhoto.id,
                SimpleDateFormat(
                    "MMMM d, yyyy",
                    Locale.ENGLISH
                ).format(Date(bestPhoto.createdAt.toLong() * 1000)),
                bestPhoto.width,
                bestPhoto.height,
                bestPhoto.url
            )
        } else {
            null
        }
    )
