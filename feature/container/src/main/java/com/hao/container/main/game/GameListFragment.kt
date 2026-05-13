package com.hao.container.main.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.container.R
import com.hao.container.databinding.FragmentGameListBinding
import com.hao.core.base.CoreBaseFragment

@Route(path = "/container/gamelist")
class GameListFragment : CoreBaseFragment<FragmentGameListBinding>() {

    private val adapter = GameListAdapter()

    private val viewModel by getViewModel(GameListViewModel::class.java) {
        gameList.observe(it) { games ->
            adapter.submitList(games)
            binding?.refreshLayout?.finishRefresh()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentGameListBinding {
        return FragmentGameListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding?.tvTitle?.setText(R.string.game_list_title)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.adapter = adapter
    }

    override fun initListener() {
        binding?.refreshLayout?.setOnRefreshListener {
            viewModel.loadGames()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGames()
    }
}
