package com.aminography.foursquareapp.presentation.ui.base

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * A class contains base functionalities of a [RecyclerView.Adapter] to make adapter classes cleaner.
 * This implementation is based on [AsyncListDiffer] which makes data changes presentation more efficient.
 *
 * @author aminography
 */
abstract class BaseAdapter<DH : BaseAdapter.BaseDataHolder, VH : BaseAdapter.BaseViewHolder> :
    RecyclerView.Adapter<VH>() {

    protected var diffUtilCallback: DiffUtil.ItemCallback<DH> = object : DiffUtil.ItemCallback<DH>() {

        override fun areItemsTheSame(new: DH, old: DH): Boolean {
            return new === old
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(new: DH, old: DH): Boolean {
            return new == old
        }
    }

    private val differ by lazy {
        AsyncListDiffer(this, diffUtilCallback)
    }

    private var onItemClickListener: OnListItemClickListener? = null

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        if (position < differ.currentList.size) {
            differ.currentList[position].apply {
                listPosition = position
                viewHolder.bindDataToView(this)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun getItem(position: Int): BaseDataHolder = differ.currentList[position]

    fun submitList(list: List<DH>) {
        differ.submitList(list)
    }

    fun setOnListItemClickListener(listener: OnListItemClickListener) {
        onItemClickListener = listener
    }

    protected fun setupClickListener(viewHolder: VH) {
        viewHolder.itemView.setOnClickListener {
            onItemClickListener?.apply {
                val position = viewHolder.adapterPosition
                if (position in 0 until differ.currentList.size) {
                    onItemClicked(differ.currentList[position])
                }
            }
        }
    }

    abstract class BaseDataHolder(var listPosition: Int = RecyclerView.NO_POSITION)

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun <DH : BaseDataHolder> bindDataToView(dataHolder: DH)
    }

}