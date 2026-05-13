package com.hao.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hao.room.databinding.ItemGameListBinding

internal data class GameRow(val title: String, val subtitle: String)

internal class GameListAdapter : ListAdapter<GameRow, GameListAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemGameListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(private val binding: ItemGameListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: GameRow) {
            binding.tvName.text = row.title
            binding.tvDesc.text = row.subtitle
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<GameRow>() {
            override fun areItemsTheSame(oldItem: GameRow, newItem: GameRow): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: GameRow, newItem: GameRow): Boolean =
                oldItem == newItem
        }
    }
}
