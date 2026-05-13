package com.hao.ui.base

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.hao.common.utils.LogUtil
import com.hao.ui.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseCenterDialogFragment<T : ViewBinding?> : DialogFragment() {
    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    private var mCompositeSubscription: CompositeDisposable? = null
    var binding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("BaseDialogFragment ->onCreate() -> ${this.javaClass.simpleName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val attr = it.attributes
            attr.gravity = Gravity.CENTER

            val width = ViewGroup.LayoutParams.WRAP_CONTENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
            it.setBackgroundDrawableResource(R.color.transparent)

            attr.windowAnimations = 0
            it.attributes = attr
        }
        dialog?.setCanceledOnTouchOutside(true)
    }

    fun show(manager: FragmentManager) {
        show(manager, this.javaClass.simpleName)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val dialog = manager.findFragmentByTag(tag)
        if (dialog != null) {
            manager.beginTransaction().remove(dialog).commitAllowingStateLoss()
        }
        super.show(manager, tag)
    }

    /**
     * 初始化布局，布局绑定
     */
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T?

    /**
     * view初始化的
     */
    abstract fun initView()

    /**
     * 点击事件监听
     */
    abstract fun initListener()

    protected open fun getCompositeSubscription(): CompositeDisposable? {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeDisposable()
        }
        return mCompositeSubscription
    }

    protected open fun addSubscription(s: Disposable?) {
        s?.let {
            getCompositeSubscription()?.add(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeSubscription?.dispose()
    }

}