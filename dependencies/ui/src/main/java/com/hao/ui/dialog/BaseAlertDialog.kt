package com.hao.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.hao.common.utils.UIUtils
import com.hao.ui.R

/**
 */
class BaseAlertDialog(
    private var mContext: Context,
    private var title: String = "",
    private var message: String = "",
    private var leftBtnText: String = mContext.getString(R.string.cancel),
    private var rightBtnText: String = mContext.getString(R.string.confirm),
    private val cancel: Boolean = true,
) {
    private var mCallBack: DialogClickCallBack? = null

    interface DialogClickCallBack {
        fun onConfirm()
        fun onCancel()
    }

    private var mAlertDialog: AlertDialog? = null

    fun show(callBack: DialogClickCallBack) {
        mCallBack = callBack
        mAlertDialog = AlertDialog.Builder(mContext).create()
        if (mContext is Activity) {
            val activity = mContext as Activity
            if (!activity.isFinishing) {
                mAlertDialog?.show()
            }
        } else {
            mAlertDialog?.show()
        }
        val contentView: View =
            LayoutInflater.from(mContext).inflate(R.layout.dialog_campfire_alert_base_layout, null)
        contentView.isFocusable = true
        contentView.isFocusableInTouchMode = true

        var mAlertDialogWindow = mAlertDialog?.window
        mAlertDialogWindow?.let {
            val attr = it.attributes
            attr.gravity = Gravity.CENTER
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
            it.setBackgroundDrawableResource(com.hao.ui.R.drawable.rectangle_radius_16dp_bg_ffffff)

            attr.width = UIUtils.dip2px(mContext, 296)
            it.attributes = attr
            it.setWindowAnimations(0)
            it.setContentView(contentView)
        }
//        mAlertDialog?.setView(contentView)
        mAlertDialog?.setCanceledOnTouchOutside(cancel)
        mAlertDialog?.setCancelable(cancel)

        var tv_title: TextView? = contentView?.findViewById(R.id.title)
        var tv_message: TextView? = contentView?.findViewById(R.id.message)
        var btnN: Button? = contentView?.findViewById(R.id.btn_n)
        var btnP: Button? = contentView?.findViewById(R.id.btn_p)

        tv_title?.isVisible = !title.isNullOrBlank()
        tv_title?.text = title

        tv_message?.isVisible = !message.isNullOrBlank()
        tv_message?.text = message

        btnN?.isVisible = !leftBtnText.isNullOrBlank()
        btnN?.text = leftBtnText

        btnP?.isVisible = !rightBtnText.isNullOrBlank()
        btnP?.text = rightBtnText

        btnN?.setOnClickListener {
            mAlertDialog?.dismiss()
            mCallBack?.onCancel()
        }
        btnP?.setOnClickListener {
            mAlertDialog?.dismiss()
            mCallBack?.onConfirm()
        }

    }

}
