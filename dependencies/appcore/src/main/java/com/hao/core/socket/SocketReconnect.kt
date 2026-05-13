package com.hao.core.socket

import com.android.socket.client.common.interfaces.basic.AbsLoopThread
import com.android.socket.client.common.interfaces.utils.ThreadUtils
import com.android.socket.client.core.utils.SLog
import com.android.socket.client.impl.exceptions.ManuallyDisconnectException
import com.android.socket.client.sdk.client.ConnectionInfo
import com.android.socket.client.sdk.client.connection.AbsReconnectionManager

class SocketReconnect : AbsReconnectionManager() {
    private var mConnectionFailedTimes = 0

    private val mReconnectTestingThread: ReconnectTestingThread = ReconnectTestingThread()
    override fun onSocketDisconnection(info: ConnectionInfo, action: String, e: Exception) {
        if (isNeedReconnect(e)) {
            reconnectDelay()
        } else {
            resetThread()
        }
    }

    override fun onSocketConnectionSuccess(info: ConnectionInfo, action: String) {
        SLog.i("SocketReconnect ,  onSocketConnectionSuccess")
        resetThread()
    }

    override fun onSocketConnectionFailed(info: ConnectionInfo, action: String, e: Exception) {
        if (e != null) {
            mConnectionFailedTimes++
            if (mConnectionFailedTimes > MAX_CONNECTION_FAILED_TIMES) {
                resetThread()
                //连接失败达到阈值,需要切换备用线路.
                val originInfo = mConnectionManager.remoteConnectionInfo
                val backupInfo = originInfo.backupInfo
                if (backupInfo != null) {
                    val bbInfo = ConnectionInfo(originInfo.ip, originInfo.port)
                    backupInfo.backupInfo = bbInfo
                    if (!mConnectionManager.isConnect) {
                        SLog.i("SocketReconnect , Prepare switch to the backup line " + backupInfo.ip + ":" + backupInfo.port + " ...")
                        synchronized(mConnectionManager) { mConnectionManager.switchConnectionInfo(backupInfo) }
                        reconnectDelay()
                    } else {
                        reconnectDelay()
                    }
                } else {
                    reconnectDelay()
                }
            } else {
                reconnectDelay()
            }
        }
    }

    private fun isNeedReconnect(e: Exception?): Boolean {
        synchronized(mIgnoreDisconnectExceptionList) {
            if (e != null && e !is ManuallyDisconnectException) { //break with exception
                val it: Iterator<Class<out Exception>> = mIgnoreDisconnectExceptionList.iterator()
                while (it.hasNext()) {
                    val classException = it.next()
                    if (classException.isAssignableFrom(e.javaClass)) {
                        return false
                    }
                }
                return true
            }
            return false
        }
    }

    @Synchronized
    private fun resetThread() {
        SLog.i("SocketReconnect , resetThread： ${mConnectionManager.isConnect}")
        mReconnectTestingThread?.shutdown()
    }

    private fun reconnectDelay() {
        synchronized(mReconnectTestingThread!!) {
            if (mReconnectTestingThread.isShutdown) {
                mReconnectTestingThread.start()
            }
        }
    }

    private inner class ReconnectTestingThread : AbsLoopThread() {
        private var mReconnectTimeDelay = 500L

        @Throws(Exception::class)
        override fun beforeLoop() {
            super.beforeLoop()

        }

        @Throws(Exception::class)
        override fun runInLoopThread() {
            if (mDetach) {
                SLog.i("SocketReconnect , ReconnectionManager already detached by framework.We decide gave up this reconnection mission!")
                shutdown()
                return
            }

            //延迟执行
            SLog.i("Reconnect after $mReconnectTimeDelay mills ...")
            ThreadUtils.sleep(mReconnectTimeDelay)
            if (mDetach) {
                SLog.i("SocketReconnect , ReconnectionManager already detached by framework.We decide gave up this reconnection mission!")
                shutdown()
                return
            }
            if (mConnectionManager.isConnect) {
                SLog.i("SocketReconnect , 连接成功： ${mConnectionManager.isConnect}")
                shutdown()
                return
            }
            val isHolden = mConnectionManager.option.isConnectionHolden
            if (!isHolden) {
                detach()
                shutdown()
                SLog.i("SocketReconnect ,  isHolden ${mConnectionManager.isConnect}")
                return
            }
            val info = mConnectionManager.remoteConnectionInfo
            SLog.i("SocketReconnect , Reconnect the server " + info.ip + ":" + info.port + " ...")
            synchronized(mConnectionManager) {
                if (!mConnectionManager.isConnect) {
                    mConnectionManager.connect()
                } else {
                    shutdown()
                    SLog.i("SocketReconnect , synchronized：shutdown")
                }
            }
        }

        override fun loopFinish(e: Exception?) {}
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        return !(o == null || javaClass != o.javaClass)
    }

    companion object {
        private const val MAX_CONNECTION_FAILED_TIMES = 50
    }
}