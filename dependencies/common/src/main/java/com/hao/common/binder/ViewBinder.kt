package com.hao.common.binder

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by wanghao 2022/7/27
 */
interface ViewBinder {
    fun initial(fragment: Fragment) {
        if (fragment.view is ViewGroup) {
            initial(fragment.view as ViewGroup)
        }
    }

    fun initial(activity: Activity) {
        if (activity.window?.decorView is ViewGroup) {
            initial(activity.window.decorView as ViewGroup)
        }
    }

    fun initial(parent: ViewGroup)

}

abstract class LifecycleViewBinder(val lifecycle: Lifecycle) : ViewBinder, LifecycleEventObserver {
    private val mCompositeDisposable = CompositeDisposable()

    fun register(subscription: Disposable?) {
        subscription?.let {
            mCompositeDisposable.add(subscription)
        }
    }

    override fun initial(parent: ViewGroup) {
        lifecycle.addObserver(this)
        initView(parent)
        initModel()
    }

    open fun initView(parent: ViewGroup) {

    }

    open fun initModel() {

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            // 忽略onCreate, 使用方应该在onCreate周期里调用 initial
            // Lifecycle.Event.ON_CREATE -> this.onCreate(source)
            Lifecycle.Event.ON_START -> this.onStart()
            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
            Lifecycle.Event.ON_STOP -> this.onStop()
            Lifecycle.Event.ON_DESTROY -> this.onDestroy()
            else -> {
            }
        }
    }

    open fun onStart() {

    }

    open fun onResume() {

    }

    open fun onPause() {

    }

    open fun onStop() {

    }

    open fun onDestroy() {
        mCompositeDisposable.clear()
        lifecycle.removeObserver(this)
    }
}
