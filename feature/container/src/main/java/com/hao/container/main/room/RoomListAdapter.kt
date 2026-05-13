package com.hao.container.main.room

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.container.databinding.ItemRoomListBinding
import com.hao.ui.widget.adapter.ViewBindingAdapter

data class RoomRow(val title: String, val subtitle: String)

class RoomListAdapter : ViewBindingAdapter<RoomRow, ItemRoomListBinding>() {

    override fun createBinding(context: Context, parent: ViewGroup, viewType: Int): ItemRoomListBinding {
        return ItemRoomListBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    override fun bindView(binding: ItemRoomListBinding, position: Int, item: RoomRow?) {
        item ?: return
        binding.tvName.text = item.title
        binding.tvDesc.text = item.subtitle
    }
}

