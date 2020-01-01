//package com.aminography.foursquareapp.presentation.ui.venues.adapter
//
//import android.view.ViewGroup
//import androidx.paging.PagedListAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.aminography.foursquareapp.domain.toVenueItemDataHolder
//import com.aminography.foursquareapp.data.datasource.local.db.venue.VenueEntity
//import com.aminography.foursquareapp.presentation.ui.venues.viewholder.VenueItemViewHolder
//
///**
// * @author aminography
// */
//class VenueListAdapterUsingPaging :
//    PagedListAdapter<VenueEntity, RecyclerView.ViewHolder>(diffUtilCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return VenueItemViewHolder(parent.context)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val dataHolder = getItem(position)
//        if (dataHolder != null) {
//            (holder as VenueItemViewHolder).bindDataToView(dataHolder.toVenueItemDataHolder())
//        }
//    }
//
//    companion object {
//        private val diffUtilCallback = object : DiffUtil.ItemCallback<VenueEntity>() {
//            override fun areItemsTheSame(
//                new: VenueEntity,
//                old: VenueEntity
//            ): Boolean {
//                return new.venueId == old.venueId
//            }
//
//            override fun areContentsTheSame(
//                new: VenueEntity,
//                old: VenueEntity
//            ): Boolean {
//                return new == old
//            }
//        }
//    }
//}