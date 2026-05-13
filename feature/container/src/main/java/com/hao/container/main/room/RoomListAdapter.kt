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

fun fakeRoomRows(): List<RoomRow> = listOf(
    RoomRow("Beginner hall", "Online · 128 / 200"),
    RoomRow("VIP room #7", "Friends only · 4 / 4"),
    RoomRow("Quick match", "Auto-fill · 3 / 5"),
    RoomRow("Tournament lobby", "Starts in 12 min"),
    RoomRow("Practice (AI)", "Solo · unlimited"),
)
