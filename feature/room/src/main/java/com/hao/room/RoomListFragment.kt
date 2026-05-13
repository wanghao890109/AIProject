package com.hao.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.core.base.CoreBaseFragment
import com.hao.room.databinding.FragmentRoomListBinding

@Route(path = "/container/roomlist")
class RoomListFragment : CoreBaseFragment<FragmentRoomListBinding>() {

    private val adapter = RoomListAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentRoomListBinding {
        return FragmentRoomListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding?.tvTitle?.setText(R.string.room_room_list_title)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.adapter = adapter
        adapter.submitList(fakeRooms())
    }

    override fun initListener() {
    }

    private fun fakeRooms(): List<RoomRow> = listOf(
        RoomRow("Beginner hall", "Online · 128 / 200"),
        RoomRow("VIP room #7", "Friends only · 4 / 4"),
        RoomRow("Quick match", "Auto-fill · 3 / 5"),
        RoomRow("Tournament lobby", "Starts in 12 min"),
        RoomRow("Practice (AI)", "Solo · unlimited"),
    )
}
