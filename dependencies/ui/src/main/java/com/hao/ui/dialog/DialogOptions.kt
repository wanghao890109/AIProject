package com.hao.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.ui.base.BaseDialogFragment
import com.hao.ui.databinding.DialogOptionsBinding

/**
 *
 */
class DialogOptions : BaseDialogFragment<DialogOptionsBinding>() {
     var title: String=""
     var tips: String=""
     var conformText: String=""
     var mCallBack: CallBack? = null

    interface CallBack {
        fun onDismiss()

        fun onClickConfirm()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): DialogOptionsBinding {
        return DialogOptionsBinding.inflate(layoutInflater)
    }


    override fun initView() {
        binding?.tvTipsTitle?.text = title
        binding?.tvTips?.text = tips
        if (!conformText.isNullOrBlank()) {
            binding?.btnConfirm?.text = conformText
        }

    }

    override fun initListener() {
        binding?.ivClose?.setOnClickListener {
            mCallBack?.onDismiss()
            dismiss()
        }

        binding?.btnConfirm?.setOnClickListener() {
            mCallBack?.onClickConfirm()
            dismiss()
        }

        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }
    }


}
