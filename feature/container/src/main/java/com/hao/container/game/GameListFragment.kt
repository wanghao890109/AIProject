package com.hao.container.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.container.R
import com.hao.container.databinding.FragmentGameListBinding
import com.hao.container.game.viewmodel.GameListViewModel
import com.hao.core.base.CoreBaseFragment

@Route(path = "/container/gamelist")
class GameListFragment : CoreBaseFragment<FragmentGameListBinding>() {

    private val viewModel by getViewModel(GameListViewModel::class.java) {
        gameList.observe(it) { list ->
            adapter.submitList(list)
        }
    }

    private val adapter = GameListAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentGameListBinding {
        return FragmentGameListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding?.tvTitle?.setText(R.string.game_list_title)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.adapter = adapter
        viewModel.loadGameList()
    }

    override fun initListener() {
    }
}
