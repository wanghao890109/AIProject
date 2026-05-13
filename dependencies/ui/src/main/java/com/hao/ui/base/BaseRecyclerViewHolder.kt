package com.hao.ui.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var context: Context? = itemView.context
}