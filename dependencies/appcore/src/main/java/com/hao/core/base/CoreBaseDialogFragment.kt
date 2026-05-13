package com.hao.core.base

import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.hao.core.viewmodel.IUIEventObserver
import com.hao.ui.base.BaseDialogFragment
import com.hao.ui.dialog.CommonProgressDialog

abstract class CoreBaseDialogFragment<T : ViewBinding?> : BaseDialogFragment<T>() , IUIEventObserver {
    override val uiLifecycleOwner: LifecycleOwner
        get() = this

    private var loadDialog: CommonProgressDialog? = null

    override fun showLoading(msg: String) {
        if (loadDialog == null) {
            loadDialog = CommonProgressDialog()
        }
        loadDialog?.let {
            if (!it.isAdded) {
                it.show(parentFragmentManager, "loading")
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
        dismissLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }
}