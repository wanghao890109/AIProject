package com.hao.common.utils

import android.view.View

abstract class OnViewClickListener : View.OnClickListener {
    private var lastTime: Long = 0
    private var delay: Long

    constructor() {
        delay = 1000
    }

    constructor(delay: Long) {
        this.delay = delay
    }

     override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < delay) {
            return
        }
        lastTime = currentTime
        onViewClick(v)
    }

    abstract fun onViewClick(v: View)
}