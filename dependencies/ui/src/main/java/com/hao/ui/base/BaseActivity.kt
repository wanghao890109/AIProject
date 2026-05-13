package com.hao.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.hao.common.utils.ActivityUtils
import com.hao.ui.R
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * @author 李敬卫 2021/11/12
 *
 */
abstract class BaseActivity<T : ViewBinding?> : RxAppCompatActivity() {
    open var binding: T? = null

    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    protected var mCompositeSubscription: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeCreate()
        super.onCreate(savedInstanceState)
        afterCreate()
        binding = getViewBinding()
        setContentView(binding?.root)
        initImmersionBar()
        mCompositeSubscription = CompositeDisposable()

        initView()
        initListener()
        initData()
    }
    /**
     * 更改语言配置
     */
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(com.hao.common.utils.language.HSLanguageUtils.attachBaseContext(newBase))
    }

    /**
     * 自定义的activti 重写次方法，不要调用super
     */
    open fun initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(getStatusBarColorResId()).statusBarDarkFont(true)
            .fitsSystemWindows(true).init()
    }

    protected open fun getStatusBarColorResId(): Int {
        return R.color.white
    }

    open fun beforeCreate() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && ActivityUtils.isTranslucentOrFloating(this)) {
            ActivityUtils.fixOrientation(this)
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏
        }
    }

    open fun afterCreate() {
        ARouter.getInstance().inject(this)
    }

    open fun getToolBar(): View? {
        return binding?.root?.findViewById(R.id.toolbar)
    }

    /**
     * 绑定布局
     */
    abstract fun getViewBinding(): T

    /**
     * view初始化的
     */
    abstract fun initView()

    /**+
     * 监听事件的方法
     */
    abstract fun initListener()

    open fun initData() {

    }
    /**
     * 销毁方法
     */
    override fun onDestroy() {
        super.onDestroy()
        binding = null
        //一旦调用了 CompositeSubscription.unsubscribe()，这个CompositeSubscription对象就不可用了,
        // 如果还想使用CompositeSubscription，就必须在创建一个新的对象了。
        mCompositeSubscription?.dispose()
    }

    open fun getCompositeSubscription(): CompositeDisposable? {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeDisposable()
        }
        return mCompositeSubscription
    }

    open fun addSubscription(s: Disposable?) {
        s?.let {
            getCompositeSubscription()?.add(it)
        }
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && ActivityUtils.isTranslucentOrFloating(
                this
            )
        ) {
            return
        }
        super.setRequestedOrientation(requestedOrientation)
    }

}