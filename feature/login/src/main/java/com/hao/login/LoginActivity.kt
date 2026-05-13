package com.hao.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.hao.common.ext.link
import com.hao.core.base.CoreBaseActivity
import com.hao.core.env.AppHttpUrls
import com.hao.login.databinding.ActivityLoginBinding
import com.hao.login.viewmodel.LoginViewModel
import com.hao.service.config.ConfigService
import com.hao.service.env.EnvironmentService

/*
* Created by wanghao 2022/7/27
*/
@Route(path = "/login/start")
class LoginActivity : CoreBaseActivity<ActivityLoginBinding>() {

    private val viewModel by getViewModel(LoginViewModel::class.java) {

    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
    }

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        binding?.loginPrivacyTip?.setMovementMethod(LinkMovementMethod.getInstance())
        binding?.loginPrivacyTip?.setHighlightColor(Color.TRANSPARENT)
        binding?.loginPrivacyTip?.setText(generateSp())
        binding?.tvSwitchEnv?.isVisible = EnvironmentService.getInstance().isDebug
    }

    override fun initListener() {
        binding?.tvSwitchEnv?.setOnClickListener {
//            val environmentDialog = ARouter.getInstance().build("/login/env").navigation()
//            if (environmentDialog is LoginEnvDialogFragment) {
//                environmentDialog.show(this.supportFragmentManager)
//            }
        }
        binding?.llGoogleLogin?.setOnClickListener {
           // viewModel.otherLogin(this, com.haiyaa.proto.LoginType.AT_Google)
        }
        binding?.llFacebookLogin?.setOnClickListener {
         //   viewModel.otherLogin(this, com.haiyaa.proto.LoginType.AT_FaceBook)
        }

        binding?.emailButton?.setOnClickListener {
            ARouter.getInstance().build("/login/email").navigation()
        }
        binding?.smsButton?.isVisible = EnvironmentService.getInstance().isTestEnv
        binding?.smsButton?.setOnClickListener {
            ARouter.getInstance().build("/login/phone").navigation()
        }
    }

    fun generateSp(): SpannableString {
        var string = getString(R.string.read_term_and_policy)
        val key1 = getString(com.hao.ui.R.string.term_of_service)
        val key2 = getString(com.hao.ui.R.string.private_policy)
        val index1: Int = string.indexOf(key1)
        val index2: Int = string.indexOf(key2)

        //需要显示的字串
        val spannedString = SpannableString(string)
        //设置点击字体颜色
        val colorSpan1 = ForegroundColorSpan(resources.getColor(com.hao.ui.R.color.grey_font_color_666))
        spannedString.setSpan(colorSpan1, index1, index1 + key1.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        val colorSpan2 = ForegroundColorSpan(resources.getColor(com.hao.ui.R.color.grey_font_color_666))
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        //设置点击事件
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                ConfigService.getInstance().getConfigCache(AppHttpUrls::class.java)?.agreementURL?.link(this@LoginActivity)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }
        spannedString.setSpan(clickableSpan1, index1, index1 + key1.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                ConfigService.getInstance().getConfigCache(AppHttpUrls::class.java)?.privacyURL?.link(this@LoginActivity)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return spannedString
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       // viewModel.onActivityResult(requestCode, resultCode, data)
    }
}