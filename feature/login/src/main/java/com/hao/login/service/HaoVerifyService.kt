package com.hao.login.service

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.common.utils.AppLifecycleManager
import com.hao.service.verify.IVerifyService
import com.hao.service.verify.VerifyListener

@Route(path = "/verify/service")
class HaoVerifyService : IVerifyService {

    override fun init(context: Context?) {

    }

    override fun verify(verifyType: Int, listener: VerifyListener?) {
//        val activity = AppLifecycleManager.getInstance().presentActivity ?: return
//        val dialog = VerifyDialog(activity, object : VerifyDialog.CallBack {
//            override fun onVerifySucceed(token: String?) {
//                listener?.onVerifySucceed(token)
//            }
//        })
//        dialog.show()
    }

    override fun verify(verifyType: Int, account: String?, listener: VerifyListener?) {
        val activity = AppLifecycleManager.getInstance().presentActivity ?: return
        if (activity !is FragmentActivity) {
            return
        }
//        val dialog = VerifyDialog2(verifyType, account ?: "", object : VerifyDialog2.CallBack {
//            override fun onVerifySucceed(token: String?) {
//                listener?.onVerifySucceed(token)
//            }
//
//        })
//        dialog.showDialog(activity.supportFragmentManager)
    }
}