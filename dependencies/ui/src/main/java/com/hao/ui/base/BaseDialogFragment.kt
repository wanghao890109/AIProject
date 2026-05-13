package com.hao.ui.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.hao.common.utils.LogUtil
import com.hao.common.utils.UIUtils
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


abstract class BaseDialogFragment<T : ViewBinding?> : DialogFragment() {
    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    var mCompositeSubscription: CompositeDisposable? = null
    private val fragmentTag: String = this.javaClass.simpleName
    var binding: T? = null
    var fragmentManagerBase: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e("BaseDialogFragment ${this.javaClass.simpleName}->onCreate()" + fragmentTag)
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
            attr.gravity = Gravity.BOTTOM

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
            it.setBackgroundDrawable(context?.let { context ->
                ContextCompat.getDrawable(
                    context,
                    com.hao.ui.R.drawable.rectangle_radius_16dp_top_bg_ffffff
                )
            })
//            var windowManager = activity?.getSystemService(WINDOW_SERVICE) as WindowManager
//            var display = windowManager.getDefaultDisplay()
//            //设置dialog高度
//            val pSize = Point()
//            display?.getSize(pSize)
//            //占用屏幕的比例
//            attr.height = (pSize.y * 0.8).toInt()

            attr.windowAnimations = 0
            it.attributes = attr

        }
        dialog?.setCanceledOnTouchOutside(true)
    }

    fun setMaxHeight() {
        dialog?.window?.let {
            val attr = it.attributes
            attr.gravity = Gravity.BOTTOM

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = com.hao.common.utils.UIUtils.getScreenHeight(context) * 7 / 10
            it.setLayout(width, height)
            attr.windowAnimations = com.hao.ui.R.style.BottomDialogAnimation
            it.attributes = attr

        }
    }

    fun showDialog(fm: FragmentManager?) {
        fragmentManagerBase = fm
        LogUtil.e("BaseDialogFragment show${fragmentTag}->onCreate()" + this@BaseDialogFragment)
        val dialog = fm?.findFragmentByTag(fragmentTag)
        if (dialog != null) {
            fm.beginTransaction().remove(dialog).commitAllowingStateLoss()
        }

        fm?.commit {
            add(this@BaseDialogFragment, fragmentTag)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val dialogFragment = fragmentManagerBase?.findFragmentByTag(fragmentTag)
        if (dialogFragment != null) {
            fragmentManagerBase?.beginTransaction()?.remove(dialogFragment)
                ?.commitAllowingStateLoss()
        }
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