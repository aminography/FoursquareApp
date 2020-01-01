package com.aminography.foursquareapp.domain

import com.aminography.foursquareapp.core.tools.formatDistance
import com.aminography.foursquareapp.data.datasource.local.db.details.VenueDetailsEntity
import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueEntity
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueDetailsResponseModel
import com.aminography.foursquareapp.data.datasource.remote.webservice.response.venue.VenueRecommendationsResponseModel
import com.aminography.foursquareapp.domain.data.VenueDetailsData
import com.aminography.foursquareapp.domain.data.VenueItemData
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

fun VenueEntity.toVenueItemData() =
    VenueItemData(
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
            price?.run {
                VenueDetailsEntity.Price(
                    tier,
                    message,
                    currency
                )
            },
            likes.count,
            VenueDetailsEntity.Rating(
                rating,
                ratingColor,
                ratingSignals
            ),
            if (tips.groups.isNotEmpty() && tips.groups[0].items.isNotEmpty()) {
                tips.groups[0].items[0].run {
                    VenueDetailsEntity.Tip(
                        createdAt,
                        text,
                        likes.count,
                        agreeCount,
                        disagreeCount,
                        user.id,
                        user.firstName,
                        user.lastName,
                        user.photoUrl()
                    )
                }
            } else null,
            popular?.isOpen,
            bestPhoto?.run {
                VenueDetailsEntity.Photo(
                    id,
                    createdAt,
                    width,
                    height,
                    url()
                )
            }
        )
    }

fun VenueDetailsEntity.toVenueDetailsData() =
    VenueDetailsData(
        venueId,
        name,
        VenueDetailsData.Contact(
            contact?.phone,
            contact?.formattedPhone,
            contact?.instagram
        ),
        VenueDetailsData.Location(
            location.address,
            location.latitude,
            location.longitude
        ),
        VenueDetailsData.Category(
            category.name,
            category.icon
        ),
        verified,
        url,
        price?.run {
            VenueDetailsData.Price(
                tier,
                message,
                currency
            )
        },
        likeCount,
        rating?.run {
            VenueDetailsData.Rating(
                rating,
                ratingColor,
                ratingSignals
            )
        },
        lastTip?.run {
            VenueDetailsData.Tip(
                SimpleDateFormat(
                    "MMMM d, yyyy",
                    Locale.ENGLISH
                ).format(Date(createdAt.toLong() * 1000)),
                text,
                likeCount,
                agreeCount,
                disagreeCount,
                userId,
                userFirstName,
                userLastName,
                userPhoto
            )
        },
        isOpen,
        bestPhoto?.run {
            VenueDetailsData.Photo(
                id,
                SimpleDateFormat(
                    "MMMM d, yyyy",
                    Locale.ENGLISH
                ).format(Date(createdAt.toLong() * 1000)),
                width,
                height,
                url
            )
        }
    )
