package com.hao.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.hao.common.utils.LogUtil
import com.hao.ui.R
import com.hao.ui.widget.BLoadingView2
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.Exception

/**
 * @author 李敬卫 2021/11/12
 *
 *  如果不想用ViewBinding 可以传Nothing  getViewBinding方法返回null
 */
abstract class BaseFragment<T : ViewBinding?> : Fragment() {
    var binding: T? = null

    private var isForbidLazyLoaded = false //是否直接加载fragment，不执行懒加载
    private var isLoaded = false //控制是否执行懒加载

    /**
     * 当前Fragment是否对用户可见
     */
    private var isVisibleToUser = false

    /**
     * 当使用ViewPager+Fragment形式会调用该方法时，setUserVisibleHint会优先Fragment生命周期函数调用，
     * 所以这个时候就,会导致在setUserVisibleHint方法执行时就执行了懒加载，
     * 而不是在onResume方法实际调用的时候执行懒加载。所以需要这个变量
     */
    private var isCallResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->onCreateView:")
        binding = getViewBinding(inflater, container,savedInstanceState)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->onViewCreated:")
        mCompositeSubscription = CompositeDisposable()
        initImmersionBar()
        initFragment()
    }

    /**
     * 自定义的activti 重写次方法，不要调用super
     */
    open fun initImmersionBar() {
        ImmersionBar.with(this)
            .titleBarMarginTop(getToolBar())
            .statusBarDarkFont(true)
            .init()
    }
    open fun getToolBar(): View? {
        return binding?.root?.findViewById(R.id.toolbar)
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->onResume()")
        isCallResume = true
        judgeLazyInit()
    }


    override fun onPause() {
        super.onPause()
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->onPause:")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isVisibleToUser = !hidden
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->onHiddenChanged()" + isVisibleToUser)
        judgeLazyInit()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->setUserVisibleHint()" + isVisibleToUser)
        judgeLazyInit()
    }

    private fun judgeLazyInit() {
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->judgeLazyInit() ！isForbidLazyLoaded()="+!isForbidLazyLoaded()+"!isLoaded =" + !isLoaded  + "++" + isCallResume)
        if (!isForbidLazyLoaded() && !isLoaded && isCallResume) {
            lazyInit()
            isLoaded = true
        }
    }

    private fun lazyInit() {
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->lazyInit")
        initView()
        initListener()
    }

    /**
     * 初始化 Api  更具需要初始化
     */
    open fun initFragment() {
        mCompositeSubscription = CompositeDisposable()
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->initFragment() isLazyLoaded()="+isForbidLazyLoaded())
        if (isForbidLazyLoaded()) {
            LogUtil.e("BaseFragment ${this.javaClass.simpleName}->initView() ")
            lazyInit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mCompositeSubscription != null) {
            mCompositeSubscription!!.dispose()
        }
        LogUtil.e("BaseFragment ${this.javaClass.simpleName}->onDestroyView()")
        binding = null
    }

    /**
     * 初始化布局，布局绑定
     */
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): T?

    /**
     * view初始化的
     */
    abstract fun initView()

    /**
     * 点击事件监听
     */
    abstract fun initListener()

    /**
     * 是否禁止懒加载
     */
   open fun isForbidLazyLoaded():Boolean {
       return isForbidLazyLoaded
    }

    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    var mCompositeSubscription: CompositeDisposable? = null

    var mProgressDialog: Dialog? = null

    //private BLoadingView mLoadingView;
    private var mBLoadingView2: com.hao.ui.widget.BLoadingView2? = null

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

    protected open fun showProgressDialog(info: CharSequence?): Dialog? {
        return showProgressDialog(info, 15_1000)
    }

    /**
     * @param autoHideDelayTime 小于500ms，则不自动隐藏
     * @return
     */
    protected open fun showProgressDialog(info: CharSequence?, autoHideDelayTime: Long): Dialog? {

        if (mProgressDialog == null) {
            mProgressDialog = Dialog(requireContext(), com.hao.ui.R.style.ProgressDialog)
            mProgressDialog!!.setCancelable(false)
            //mLoadingView = new BLoadingView(getContext());
            mBLoadingView2 = com.hao.ui.widget.BLoadingView2(context)
            mProgressDialog!!.setContentView(mBLoadingView2!!)
            //            mProgressDialog.setContentView(mLoadingView);
        }

//        if (mLoadingView != null) {
//            mLoadingView.setText(info.toString());
//        }
        if (isDetached) {
            return mProgressDialog
        }
        if (!mProgressDialog!!.isShowing) {
            try {
                mProgressDialog!!.show()
                showLoadingDialogView(autoHideDelayTime)
            } catch (e: Exception) {
            }
        }
        return mProgressDialog
    }

    protected open fun hideProgressDialog(): Dialog? {
        hideLoadingDialogView()
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            try {
                mProgressDialog!!.dismiss()
            } catch (t: Throwable) {
                LogUtil.e("error:${t.message}")
            }
        }
        return mProgressDialog
    }

    open fun isProgressDialogShowing(): Boolean {
        return mProgressDialog != null && mProgressDialog!!.isShowing
    }

    open fun showLoadingDialogView(autoHideDelayTime: Long) {
        if (mBLoadingView2 != null) {
            val MinDelayTime: Long = 500
            mBLoadingView2!!.setVisibility(View.INVISIBLE)
            mBLoadingView2!!.removeCallbacks(mShowLoadingDialogViewRunnable)
            mBLoadingView2!!.postDelayed(mShowLoadingDialogViewRunnable, MinDelayTime)
            mBLoadingView2!!.removeCallbacks(mHideLoadingDialogViewRunnable)
            if (autoHideDelayTime > MinDelayTime) {
                mBLoadingView2!!.postDelayed(
                    mHideLoadingDialogViewRunnable,
                    autoHideDelayTime
                ) //容错一下，最多显示15s
            }
        }
    }

    open fun hideLoadingDialogView() {
        if (mBLoadingView2 != null) {
            mBLoadingView2!!.removeCallbacks(mHideLoadingDialogViewRunnable)
            mBLoadingView2!!.removeCallbacks(mShowLoadingDialogViewRunnable)
            mBLoadingView2!!.setVisibility(View.INVISIBLE)
        }
    }

    val mShowLoadingDialogViewRunnable = Runnable {
        if (mBLoadingView2 != null) {
            mBLoadingView2!!.setVisibility(View.VISIBLE)
        }
    }

    val mHideLoadingDialogViewRunnable = Runnable { hideProgressDialog() }


}