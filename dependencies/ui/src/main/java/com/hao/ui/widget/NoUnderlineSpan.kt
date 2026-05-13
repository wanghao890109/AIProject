package com.hao.ui.widget

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * 无下划线
 */
open class NoUnderlineSpan : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.setColor(ds.linkColor)

        ds.setUnderlineText(false)
    }

    override fun onClick(widget: View) {

    }
}