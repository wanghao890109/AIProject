package com.hao.ui.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.hao.common.utils.ContextUtils
import com.hao.ui.R

/**
 *二次确认弹窗
 *
 * @author 李敬卫 2022-05-17
 */
class AlertBuilderDialog(
    context: Context,
    private val title: String = "",
    private val message: String = "",
    private val leftBtnText: String = context.getString(R.string.cancel),
    private val rightBtnText: String = context.getString(R.string.confirm),
) : AlertDialog(context) {

    private var positiveClickListener: DialogInterface.OnClickListener? = null
    private var negativeClickListener: DialogInterface.OnClickListener? = null
    fun setPositiveClickListener(positiveListener: DialogInterface.OnClickListener?) {
        positiveClickListener = positiveListener
    }

    fun setNegativeClickListener(negativeListener: DialogInterface.OnClickListener?) {
        negativeClickListener = negativeListener
    }

    fun showDialog() {
        if (!ContextUtils.isViewDestroy(context)) {
            val builderDialog = Builder(context).setTitle(title).setMessage(message)
                .setNegativeButton(leftBtnText, negativeClickListener)
                .setPositiveButton(rightBtnText, positiveClickListener)
            builderDialog.show()
        }
    }
}

/**
 * 仅确定弹窗
 */
class AlertBuilderOKDialog(
    context: Context,
    private val title: String = "",
    private val message: String = "",
    private val rightBtnText: String = context.getString(R.string.confirm),
) : AlertDialog(context) {

    private var positiveClickListener: DialogInterface.OnClickListener? = null
    fun setPositiveClickListener(positiveListener: DialogInterface.OnClickListener?) {
        positiveClickListener = positiveListener
    }

    fun showDialog() {
        if (!ContextUtils.isViewDestroy(context)) {
            val builderDialog = Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(rightBtnText, positiveClickListener)
            builderDialog.show()
        }
    }
}