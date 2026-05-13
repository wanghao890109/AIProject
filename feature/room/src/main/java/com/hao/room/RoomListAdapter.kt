package com.hao.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hao.room.databinding.ItemRoomListBinding

internal data class RoomRow(val title: String, val subtitle: String)

internal class RoomListAdapter : ListAdapter<RoomRow, RoomListAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemRoomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(private val binding: ItemRoomListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: RoomRow) {
            binding.tvName.text = row.title
            binding.tvDesc.text = row.subtitle
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<RoomRow>() {
            override fun areItemsTheSame(oldItem: RoomRow, newItem: RoomRow): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: RoomRow, newItem: RoomRow): Boolean =
                oldItem == newItem
        }
    }
}
