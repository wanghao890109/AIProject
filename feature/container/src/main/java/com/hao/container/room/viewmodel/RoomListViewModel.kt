package com.hao.container.room.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hao.container.room.RoomRow
import com.hao.core.viewmodel.BaseViewModel

class RoomListViewModel : BaseViewModel() {

    val roomList = MutableLiveData<List<RoomRow>>()

    fun loadRoomList() {
        roomList.value = listOf(
            RoomRow("Beginner hall", "Online · 128 / 200"),
            RoomRow("VIP room #7", "Friends only · 4 / 4"),
            RoomRow("Quick match", "Auto-fill · 3 / 5"),
            RoomRow("Tournament lobby", "Starts in 12 min"),
            RoomRow("Practice (AI)", "Solo · unlimited"),
        )
    }
}
