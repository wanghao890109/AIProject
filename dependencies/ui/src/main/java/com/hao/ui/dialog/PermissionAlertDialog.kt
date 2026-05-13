package com.hao.ui.dialog

import android.content.Context
import com.hao.ui.R


/**
 * @author Li 2022-05-30
 */
class PermissionAlertDialog(
    private var context: Context,
    private var title: String = "",
    private var message: String = "",
    private var leftBtnText: String = context.getString(R.string.cancel),
    private var  rightBtnText: String = context.getString(R.string.settings)) {

    interface CallBack {
        fun onConfirm()
        fun onDismiss()
    }
    fun show(mCallBack: CallBack?){
        val dialog = BaseAlertDialog(context, title, message, leftBtnText, rightBtnText, false)
        dialog.show(object : BaseAlertDialog.DialogClickCallBack{
            override fun onConfirm() {
                mCallBack?.onConfirm()
            }

            override fun onCancel() {
                mCallBack?.onDismiss()
            }
        })
    }
}