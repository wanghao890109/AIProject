package com.hao.core.base


import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.hao.core.viewmodel.IUIEventObserver
import com.hao.ui.base.BaseActivity
import com.hao.ui.dialog.CommonProgressDialog

/**
 * Created by wanghao 2022/7/27
 */
@SuppressLint("Registered")
abstract class CoreBaseActivity<T : ViewBinding?> : BaseActivity<T?>(), IUIEventObserver {

    override val uiLifecycleOwner: LifecycleOwner
        get() = this

    private var loadDialog: CommonProgressDialog? = null

    override fun showLoading(msg: String) {
        if (loadDialog == null) {
            loadDialog = CommonProgressDialog()
        }
        loadDialog?.let {
            if (!it.isAdded) {
                it.show(supportFragmentManager, "loading")
            }
        }
    }

    override fun dismissLoading() {
        loadDialog?.let {
            if (it.isAdded) {
                it.dismissAllowingStateLoss()
            }
        }
    }

    override fun showToast(msg: String) {
        if (msg.isNotBlank()) {
            com.hao.common.utils.ToastUtils.showShort(msg)
        }
    }

    override fun finishView() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }
}