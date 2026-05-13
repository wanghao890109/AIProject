package com.hao.container.init.module

import android.app.Application
import com.hao.common.utils.ThreadUtil
import com.hao.core.socket.SocketManager
import com.hao.service.account.AccountListener
import com.hao.service.account.AccountService
import com.hao.service.account.IAccountService
import com.hao.service.account.LoginType
import com.hao.proto.LoginInfo
import com.hao.proto.PushCmd
import com.hao.proto.RoomCmd
import okio.ByteString

class SocketInit : com.hao.core.moduleinit.ModuleInit() {

    override fun tag(): String {
        return "SocketInit"
    }

    override fun init(application: Application?) {

        AccountService.getInstance().register(object : AccountListener {
            override fun onLogin(sender: IAccountService?, type: LoginType?) {
                initSocket()
            }

            override fun onLogout(sender: IAccountService?) {
                destroy()
            }

            override fun onUpdated(sender: IAccountService?) {

            }
        })

        initSocket()
    }

    override fun asyncInit(application: Application?) {

    }

    private fun initSocket() {
        if (AccountService.getInstance().isLogin) {
            SocketManager.get().connectStateChange.observeForever {

            }
            val loginInfo = AccountService.getInstance().getAccountInfo(LoginInfo::class.java)
            SocketManager.get().connect(loginInfo.user.userId, loginInfo.gateAddr)
            SocketManager.get().setPushReceiver(object : SocketManager.PushReceiver {
                override fun onPush(cmd: Int, data: ByteString) {
                    ThreadUtil.runOnMainThread {
                        handle(cmd, data)
                    }
                }

                private fun handle(cmd: Int, data: ByteString) {
                    if (RoomCmd.fromValue(cmd) != null) {

                    } else if (PushCmd.fromValue(cmd) != null) {

                    }
                }
            })
        }
    }

    private fun destroy() {
        SocketManager.get().destroy()
    }
}