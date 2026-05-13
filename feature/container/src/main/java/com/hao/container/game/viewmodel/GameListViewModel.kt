package com.hao.container.game.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hao.container.game.GameRow
import com.hao.core.viewmodel.BaseViewModel

class GameListViewModel : BaseViewModel() {

    val gameList = MutableLiveData<List<GameRow>>()

    fun loadGameList() {
        gameList.value = listOf(
            GameRow("Chess", "Classic board · 1v1"),
            GameRow("Texas Hold'em", "Cards · 2–9 players"),
            GameRow("Go", "Board · ranked"),
            GameRow("Mahjong", "Tiles · 4 players"),
            GameRow("UNO", "Quick party · 4–10 players"),
            GameRow("Sudoku", "Solo puzzle · daily challenge"),
        )
    }
}
