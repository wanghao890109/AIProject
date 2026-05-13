package com.hao.ui.ext

import android.view.View

/**
 * @author 李敬卫 2022/10/25
 *
 */

var lastTime: Long = 0
var delayTime: Long = 500

/**
 * 防止重复点
 */
fun <T : View> T.clickView(delay: Long = 500, block: (T) -> Unit) {
    delayTime = delay
    setOnClickListener {
        if (clickEnable()) {
            block(this)
        }
    }
}

private fun clickEnable(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - lastTime >= delayTime) {
        clickable = true
    }
    lastTime = currentClickTime
    return clickable
}