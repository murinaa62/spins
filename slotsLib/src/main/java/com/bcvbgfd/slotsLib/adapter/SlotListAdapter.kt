package com.bcvbgfd.slotsLib.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bcvbgfd.slotsLib.SlotRecyclerView
import com.bcvbgfd.slotsLib.databinding.SlotListBinding
import com.bcvbgfd.test.utils.inflate
import com.bumptech.glide.Glide

class SlotListAdapter: ListAdapter<SlotRecyclerView.Slot, SlotListAdapter.Holder>(SlotDiffUtilCallback()) {

    class Holder(
        private val binding: SlotListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SlotRecyclerView.Slot) {
            Glide.with(itemView).load(item.image).into(binding.image)
        }
    }

    class SlotDiffUtilCallback: DiffUtil.ItemCallback<SlotRecyclerView.Slot>() {
        override fun areItemsTheSame(oldItem: SlotRecyclerView.Slot, newItem: SlotRecyclerView.Slot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SlotRecyclerView.Slot, newItem: SlotRecyclerView.Slot): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(SlotListBinding::inflate))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }

}