package com.hao.ui.widget.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Created by wanghao 2022/11/9
 */
abstract class ViewBindingAdapter<T, DB : ViewBinding> : BaseQuickAdapter<T, ViewBindingHolder<DB>>() {

    override fun onBindViewHolder(holder: ViewBindingHolder<DB>, position: Int, item: T?) {
        bindView(holder.binding, position, item)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ViewBindingHolder<DB> {
        return ViewBindingHolder(
            createBinding(
                context,
                parent,
                viewType
            )
        )
    }

    abstract fun createBinding(context: Context, parent: ViewGroup, viewType: Int): DB

    abstract fun bindView(binding: DB, position: Int, item: T?)

}