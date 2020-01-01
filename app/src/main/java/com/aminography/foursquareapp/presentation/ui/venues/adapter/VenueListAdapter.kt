package com.aminography.foursquareapp.presentation.ui.venues.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter
import com.aminography.foursquareapp.presentation.ui.venues.dataholder.VenueItemDataHolder
import com.aminography.foursquareapp.presentation.ui.venues.viewholder.VenueItemViewHolder

/**
 * A concrete instance of adapter for list of the venue recommendations
 *
 * @author aminography
 */
class VenueListAdapter : BaseAdapter<VenueItemDataHolder, VenueItemViewHolder>() {

    init {
        diffUtilCallback = object : DiffUtil.ItemCallback<VenueItemDataHolder>() {
            override fun areItemsTheSame(
                new: VenueItemDataHolder,
                old: VenueItemDataHolder
            ): Boolean {
                return new.venueId == old.venueId
            }

            override fun areContentsTheSame(
                new: VenueItemDataHolder,
                old: VenueItemDataHolder
            ): Boolean {
                return new == old
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueItemViewHolder {
        return VenueItemViewHolder(parent.context).also { setupClickListener(it) }
    }

}
