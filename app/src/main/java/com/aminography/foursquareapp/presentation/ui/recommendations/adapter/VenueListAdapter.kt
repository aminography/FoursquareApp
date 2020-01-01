package com.aminography.foursquareapp.presentation.ui.recommendations.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter
import com.aminography.foursquareapp.presentation.ui.recommendations.dataholder.VenueItemDataHolder
import com.aminography.foursquareapp.presentation.ui.recommendations.viewholder.VenueItemViewHolder

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
                return new.data.venueId == old.data.venueId
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
