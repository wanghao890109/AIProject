package com.hao.core.ui.webview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClipboardUtils
import com.github.lzyzsd.jsbridge.BridgeHandler
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.hao.appcore.databinding.WebviewActivityBinding
import com.hao.common.ext.ellipsis
import com.hao.common.ext.link
import com.hao.common.utils.ThreadUtil
import com.hao.common.utils.UIUtils
import com.hao.service.account.AccountService
import com.hao.service.env.EnvironmentService
import com.hao.core.ui.webview.action.CopyTextAction
import com.hao.core.ui.webview.action.GetUserInfoAction
import com.hao.core.ui.webview.action.OpenPageAction
import com.hao.core.ui.webview.action.SetTopBarAction
import com.hao.core.ui.webview.action.WebViewAction
import com.hao.ui.base.BaseActivity
import com.hao.ui.ext.load
import com.hao.core.ui.webview.action.*

@Route(path = "/view/web")
class WebViewActivity : BaseActivity<WebviewActivityBinding>(), BridgeHandler {

    @Autowired
    @JvmField
    var url: String = ""

    @Autowired
    @JvmField
    var title: String = ""

    //toolbar=0        0 默认显示返回+标题， 1 只显示返回（返回按钮盖在webview上） 2 toolbar啥都不显示
    @Autowired
    @JvmField
    var toolbar: Int = 0

    //宽高比
    // heightRatio=0.6     大于0小于1：半屏 ，其它全屏
    @Autowired
    @JvmField
    var heightRatio: Float = 1f

    private val gson = Gson()

    /**
     * 绑定布局
     */
    override fun getViewBinding(): WebviewActivityBinding {
        return WebviewActivityBinding.inflate(layoutInflater)
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(getStatusBarColorResId())
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .keyboardEnable(true)
            .init()
    }
    /**
     * view初始化的
     */
    override fun initView() {

        if (heightRatio > 0f && heightRatio < 1f) {
            //半屏
            val screenHeight = UIUtils.getScreenHeight(this)
            val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (screenHeight * heightRatio).toInt())
            layoutParams.gravity = Gravity.BOTTOM
            binding?.container?.layoutParams = layoutParams
            //半屏不显示titile
            binding?.toolbar?.root?.isVisible = false
        } else {
            // 0 默认显示返回+标题， 1 只显示返回（返回按钮盖在webview上） 2 toolbar啥都不显示
            val topBarAction = SetTopBarAction()
            if (toolbar == 0) {
                //topBarAction 默认值
                topBarAction.layout = 0
            } else if (toolbar == 1) {
                topBarAction.hideTitle = true
                topBarAction.layout = 1
            } else if (toolbar == 2) {
                topBarAction.layout = -1
            }
            setTopBar(topBarAction)
        }
        binding?.toolbar?.tvTitle?.text = title?.ellipsis(this)

        binding?.webview?.setBackgroundColor(Color.TRANSPARENT)
        binding?.webview?.settings?.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            allowFileAccess = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            val ua = userAgentString
            userAgentString = "$ua; haiyaa/${EnvironmentService.getInstance().version}; OS/Android"
        }
        binding?.webview?.registerHandler("JSToHeyHeyBridge", this)
        binding?.webview?.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                binding?.toolbar?.tvTitle?.text = title?.ellipsis(this@WebViewActivity)
            }
        }
        binding?.webview?.webViewClient = object : BridgeWebViewClient(binding?.webview) {

        }

        binding?.webview?.loadUrl(url)
    }

    private fun setTopBar(setting: SetTopBarAction) {
        binding?.webview?:return
        val layoutParams = binding?.webview?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = binding?.toolbar?.root?.id ?: ConstraintLayout.LayoutParams.PARENT_ID
        if (setting.layout == -1) {
            binding?.toolbar?.root?.isVisible = false
        } else if (setting.layout == 1) {
            binding?.toolbar?.root?.isVisible = true
            binding?.toolbar?.titleBarBg?.setBackgroundColor(Color.TRANSPARENT)
            layoutParams.topToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        } else {
            binding?.toolbar?.root?.isVisible = true
        }
        binding?.webview?.layoutParams = layoutParams

        binding?.toolbar?.ivBack?.isVisible = !setting.hideBackBtn
        binding?.toolbar?.tvTitle?.isVisible = !setting.hideTitle
        if (TextUtils.isEmpty(setting.rightBtnIconUrl)) {
            binding?.toolbar?.ivSetting?.isVisible = false
        } else {
            binding?.toolbar?.ivSetting?.isVisible = true
            binding?.toolbar?.ivSetting?.load(setting.rightBtnIconUrl)
            binding?.toolbar?.ivSetting?.setOnClickListener {
                setting.rightBtnJumpUrl?.link(this)
            }
        }
    }

    /**+
     * 监听事件的方法
     */
    override fun initListener() {
        binding?.toolbar?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.flRoot?.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.webview?.unregisterHandler("JSToHeyHeyBridge")
        binding?.webview?.destroy()
    }

    override fun onBackPressed() {
        if (binding?.webview?.canGoBack() == true) {
            binding?.webview?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun handler(data: String?, function: CallBackFunction?) {
        val action = gson.fromJson(data, WebViewAction::class.java)
        ThreadUtil.runOnMainThread {
            val webAction = receiveWebAction(action)
            function?.onCallBack(gson.toJson(webAction))
        }
    }

    private fun receiveWebAction(webAction: WebViewAction?): WebViewAction? {
        webAction?.action ?: return null
        if (webAction.action == WebViewConst.ACTION_CLOSE_PAGE) {
            finish()
        } else if (webAction.action == WebViewConst.ACTION_OPEN_PAGE) {
            val openPage = gson.fromJson(webAction.data, OpenPageAction::class.java)
            openPage.url?.link(this)
        } else if (webAction.action == WebViewConst.ACTION_COPY_TEXT) { //复制文本
            val copyText = gson.fromJson(webAction.data, CopyTextAction::class.java)
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("text", copyText.content))
        } else if (webAction.action == WebViewConst.ACTION_GET_COPY_TEXT) { //获取黏贴版文本
            val copyText = CopyTextAction(ClipboardUtils.getText().toString())
            webAction.data = copyText.toJson()
        } else if (webAction.action == WebViewConst.ACTION_SET_TOP_BAR) {
            val topBar = gson.fromJson(webAction.data, SetTopBarAction::class.java)
            setTopBar(topBar)
        } else if (webAction.action == WebViewConst.ACTION_GET_USER_INFO) {
            val userInfo = GetUserInfoAction()
            val account = AccountService.getInstance()
            userInfo.uid = account.userId
            userInfo.accId = account.accId
            userInfo.icon = account.userIcon
            userInfo.name = account.userName
            userInfo.token = account.token
            webAction.data = userInfo.toJson()
        }
//        if (EnvironmentService.getInstance().isDebug) {
//            ToastUtils.showShort(gson.toJson(webAction))
//        }
        return webAction
    }

}