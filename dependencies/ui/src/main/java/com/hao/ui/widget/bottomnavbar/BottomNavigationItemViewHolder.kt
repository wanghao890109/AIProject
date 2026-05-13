package com.hao.ui.widget.bottomnavbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class BottomNavigationItemViewHolder(@JvmField var itemInfo: BottomNavigationItem) {
    abstract val layoutId: Int
    var itemView: View? = null
    @JvmField
    var position:Int = -1
    @JvmField
    var isSelected = false

    var isShowAnimation = true

    open fun createItemView(parent: ViewGroup): View{
        itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        initItemView(itemView!!)
        return itemView!!
    }

    open fun initItemView(view: View) {
    }

    abstract fun bindItemView()

    abstract fun onSelect()

    abstract fun unSelect()

    abstract fun setUnreadCount(unReadCount:Int)

    open fun reset() {
        position = -1
        isSelected = false
    }

}

fun String?.emptyNull(default: String = ""): String {
    if (this.isNullOrBlank()) return default
    return this
}
