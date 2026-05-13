package com.hao.container.main.game

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.container.databinding.ItemGameListBinding
import com.hao.ui.widget.adapter.ViewBindingAdapter

data class GameRow(val title: String, val subtitle: String)

class GameListAdapter : ViewBindingAdapter<GameRow, ItemGameListBinding>() {

    override fun createBinding(context: Context, parent: ViewGroup, viewType: Int): ItemGameListBinding {
        return ItemGameListBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    override fun bindView(binding: ItemGameListBinding, position: Int, item: GameRow?) {
        item ?: return
        binding.tvName.text = item.title
        binding.tvDesc.text = item.subtitle
    }
}

fun fakeGameRows(): List<GameRow> = listOf(
    GameRow("Chess", "Classic board · 1v1"),
    GameRow("Texas Hold'em", "Cards · 2–9 players"),
    GameRow("Go", "Board · ranked"),
    GameRow("Mahjong", "Tiles · 4 players"),
    GameRow("UNO", "Quick party · 4–10 players"),
    GameRow("Sudoku", "Solo puzzle · daily challenge"),
)
