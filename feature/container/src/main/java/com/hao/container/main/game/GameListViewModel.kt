package com.hao.container.main.game

import androidx.lifecycle.MutableLiveData
import com.hao.core.viewmodel.BaseViewModel

class GameListViewModel : BaseViewModel() {

    val gameList = MutableLiveData<List<GameRow>>()

    fun loadGames() {
        gameList.value = fakeGameRows()
    }
}

private fun fakeGameRows(): List<GameRow> = listOf(
    GameRow("Chess", "Classic board · 1v1"),
    GameRow("Texas Hold'em", "Cards · 2–9 players"),
    GameRow("Go", "Board · ranked"),
    GameRow("Mahjong", "Tiles · 4 players"),
    GameRow("UNO", "Quick party · 4–10 players"),
    GameRow("Sudoku", "Solo puzzle · daily challenge"),
)
