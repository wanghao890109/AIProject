package com.hao.ui.widget.divider

import android.content.Context
import android.graphics.Color
import com.hao.common.utils.UIUtils

class HorizontalDividerItemDecoration(context: Context?) : DividerItemDecoration() {
    init {
        val build: Divider = Divider.Builder()
            .size(UIUtils.dip2px(context, 1))
            .color(Color.parseColor("#F8F9FE"))
            .build()

        setDividerLookup(object : DividerLookup {
            override fun getVerticalDivider(position: Int): Divider? {
                return null
            }

            override fun getHorizontalDivider(position: Int): Divider? {
                return build
            }
        })
    }
}
