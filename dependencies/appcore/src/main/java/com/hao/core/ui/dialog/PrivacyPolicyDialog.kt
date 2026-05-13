package com.hao.core.ui.dialog

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.hao.appcore.R
import com.hao.service.env.EnvironmentService
import com.hao.ui.base.BaseCenterDialogFragment
import com.hao.ui.databinding.DialogPrivacyPolicyBinding

/**
 * showType 0 首页启动弹窗 1 登录弹窗
 */
class PrivacyPolicyDialog(private val showType: Int) :
    BaseCenterDialogFragment<DialogPrivacyPolicyBinding>() {

    private var mCallBack: PrivacyCallBack? = null

    interface PrivacyCallBack {
        fun agree()
        fun disagree()
        fun userAgreeClick()
        fun privacyClick()
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): DialogPrivacyPolicyBinding {
        return DialogPrivacyPolicyBinding.inflate(layoutInflater)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val attr = it.attributes
            attr.gravity = Gravity.CENTER
            it.setBackgroundDrawableResource(com.hao.ui.R.color.transparent)

            val width = com.hao.common.utils.UIUtils.getScreenWidth(context) * 8 / 10
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
            attr.windowAnimations = 0
            it.attributes = attr
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    override fun initView() {
        var privacyContent = ""
        if (showType == 0) {
            binding?.tvPrivacyTitle?.text = EnvironmentService.getInstance().appName
            privacyContent = getString(R.string.privacy_policy_content)
        } else if (showType == 1) {
            val left = com.hao.common.utils.UIUtils.dip2px(context, 15)
            val params = binding?.tvPrivacyMessage?.layoutParams as ConstraintLayout.LayoutParams
            params.marginStart = left
            params.marginEnd = left
            binding?.tvPrivacyTitle?.text = getString(R.string.user_agreement_privacy)
            privacyContent = getString(R.string.read_privacy_content)
        }

        val userAgreementKey = getString(R.string.user_agreement_key)
        val privacyPolicyKey = getString(R.string.privacy_policy_key)

        val userAgreeKeyIndex = privacyContent.indexOf(userAgreementKey)
        val policyKeyIndex = privacyContent.indexOf(privacyPolicyKey)

        val spannableString = SpannableString(privacyContent)
        context?.let {
            val color = ContextCompat.getColor(it, com.hao.ui.R.color.blue_font_color)
            val spanUserAgree = ForegroundColorSpan(color)
            val spanPolicy = ForegroundColorSpan(color)

            spannableString.setSpan(
                spanUserAgree,
                userAgreeKeyIndex,
                userAgreeKeyIndex + userAgreementKey.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                spanPolicy,
                policyKeyIndex,
                policyKeyIndex + privacyPolicyKey.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        mCallBack?.userAgreeClick()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                    }
                },
                userAgreeKeyIndex + 1,
                userAgreeKeyIndex + userAgreementKey.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        mCallBack?.privacyClick()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                    }
                },
                policyKeyIndex,
                policyKeyIndex + privacyPolicyKey.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }

        //设置点击后的颜色为透明，否则会一直出现高亮
        binding?.tvPrivacyMessage?.highlightColor = Color.TRANSPARENT
        binding?.tvPrivacyMessage?.movementMethod = LinkMovementMethod.getInstance()
        binding?.tvPrivacyMessage?.text = spannableString
    }

    override fun initListener() {
        binding?.btnAgree?.setOnClickListener {
            mCallBack?.agree()
            dismiss()
        }

        binding?.btnDisagree?.setOnClickListener {
            mCallBack?.disagree()
            dismiss()
        }

    }

    fun show(fm: FragmentManager?, callBack: PrivacyCallBack? = null) {
        this.mCallBack = callBack
        fm?.let {
            super.show(fm)
        }
    }

}