package com.aminography.foursquareapp.presentation.ui.recommendations.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.aminography.foursquareapp.R
import com.aminography.foursquareapp.core.tools.splitVenueName
import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter
import com.aminography.foursquareapp.presentation.ui.loadImage
import com.aminography.foursquareapp.presentation.ui.recommendations.dataholder.VenueItemDataHolder
import kotlinx.android.synthetic.main.list_item_venue.view.*
import java.util.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
class VenueItemViewHolder(context: Context) :
    BaseAdapter.BaseViewHolder(
        LayoutInflater.from(context).inflate(R.layout.list_item_venue, null)
    ) {

    override fun <DH : BaseAdapter.BaseDataHolder> bindDataToView(dataHolder: DH) {
        if (dataHolder is VenueItemDataHolder) {
            with(itemView) {
                dataHolder.data.apply {
                    val name = splitVenueName(name).first
                    nameTextView.text = name

                    distanceTextView.text = if (address.isNullOrEmpty()) {
                        distance
                    } else {
                        String.format(Locale.getDefault(), "%s,", distance)
                    }

                    addressTextView.text = address
                    addressTextView.visibility =
                        if (address.isNullOrEmpty()) View.GONE
                        else View.VISIBLE

                    verifiedImageView.visibility =
                        if (verified) View.VISIBLE
                        else View.GONE

                    imageView.loadImage(
                        imageUrl = categoryIcon,
                        crossFade = true
                    )
                }
            }
        }
    }

}