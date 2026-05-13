package com.hao.container.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.container.R
import com.hao.container.databinding.FragmentRoomListBinding
import com.hao.container.room.viewmodel.RoomListViewModel
import com.hao.core.base.CoreBaseFragment

@Route(path = "/container/roomlist")
class RoomListFragment : CoreBaseFragment<FragmentRoomListBinding>() {

    private val viewModel by getViewModel(RoomListViewModel::class.java) {
        roomList.observe(it) { list ->
            adapter.submitList(list)
        }
    }

    private val adapter = RoomListAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentRoomListBinding {
        return FragmentRoomListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding?.tvTitle?.setText(R.string.room_list_title)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.adapter = adapter
        viewModel.loadRoomList()
    }

    override fun initListener() {
    }
}
