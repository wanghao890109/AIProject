package com.hao.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.core.base.CoreBaseFragment
import com.hao.room.databinding.FragmentGameListBinding

@Route(path = "/container/gamelist")
class GameListFragment : CoreBaseFragment<FragmentGameListBinding>() {

    private val adapter = GameListAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentGameListBinding {
        return FragmentGameListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding?.tvTitle?.setText(R.string.room_game_list_title)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.adapter = adapter
        adapter.submitList(fakeGames())
    }

    override fun initListener() {
    }

    private fun fakeGames(): List<GameRow> = listOf(
        GameRow("Chess", "Classic board · 1v1"),
        GameRow("Texas Hold'em", "Cards · 2–9 players"),
        GameRow("Go", "Board · ranked"),
        GameRow("Mahjong", "Tiles · 4 players"),
        GameRow("UNO", "Quick party · 4–10 players"),
        GameRow("Sudoku", "Solo puzzle · daily challenge"),
    )
}
