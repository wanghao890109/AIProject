package com.hao.ui.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hao.ui.R

/**
 * Created by wanghao 2022/8/1
 */
class CommonProgressDialog : DialogFragment() {

    private var msg: String? = null

    private var rootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.LoadingDialogStyle)
        isCancelable = false
    }

    override fun onStart() {
        super.onStart()
/*        val window = dialog!!.window
        val windowParams = window!!.attributes
        windowParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        windowParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        windowParams.dimAmount = 0.0f
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.attributes = windowParams*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.base_loading_layout, container, false)
        showProgressDelay()
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
    }

    private fun showProgressDelay() {
        hideProgress()
        rootView?.postDelayed(showLoadingRunnable, 500)
        //这里延迟显示，500ms内的loading不显示
        rootView?.postDelayed(hideLoadingRunnable, 15 * 1000)
    }

    private fun hideProgress() {
        rootView?.visibility = View.INVISIBLE
        rootView?.removeCallbacks(showLoadingRunnable)
        rootView?.removeCallbacks(hideLoadingRunnable)
    }

    private val showLoadingRunnable = Runnable {
        rootView?.visibility = View.VISIBLE
    }

    private val hideLoadingRunnable = Runnable {
        if (isAdded) {
            try {
                dismissAllowingStateLoss()
            } catch (e: Exception) {
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().remove(this)?.commitAllowingStateLoss()
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}