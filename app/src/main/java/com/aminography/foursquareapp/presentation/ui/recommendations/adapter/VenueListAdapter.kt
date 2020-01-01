package com.aminography.foursquareapp.presentation.ui.recommendations.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.aminography.foursquareapp.domain.data.VenueItemData
import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter
import com.aminography.foursquareapp.presentation.ui.recommendations.viewholder.VenueItemViewHolder

/**
 * A concrete instance of adapter for list of the venue recommendations
 *
 * @author aminography
 */
class VenueListAdapter : BaseAdapter<VenueItemData, VenueItemViewHolder>() {

    init {
        diffUtilCallback = object : DiffUtil.ItemCallback<VenueItemData>() {
            override fun areItemsTheSame(
                new: VenueItemData,
                old: VenueItemData
            ): Boolean {
                return new.venueId == old.venueId
            }

            override fun areContentsTheSame(
                new: VenueItemData,
                old: VenueItemData
            ): Boolean {
                return new == old
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueItemViewHolder {
        return VenueItemViewHolder(parent.context).also { setupClickListener(it) }
    }

}
