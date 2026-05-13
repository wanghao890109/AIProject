package com.hao.core.download

import com.liulishuo.okdownload.DownloadContext
import com.liulishuo.okdownload.DownloadContextListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist
import com.hao.common.utils.LogUtil
import com.hao.service.env.EnvironmentService
import java.io.File


/**
 * Created by wanghao 2021/1/21 10:44
 */
class DownloadManager private constructor() {

    companion object {
        private var manager: DownloadManager? = null
            get() {
                if (field == null) {
                    field = DownloadManager()
                }
                return field
            }

        @Synchronized
        fun get(): DownloadManager {
            return manager!!
        }
    }

    fun initTasks(
        urls: List<String>,
        listener: DownloadContextListener,
        folder: String = "",
        suffix: String = ""
    ): DownloadContext {
        val parentFile = if (folder.isNullOrEmpty()) {
            val context = EnvironmentService.getInstance().context
            File(context!!.filesDir.absolutePath)
        } else {
            File(folder)
        }
        val set = DownloadContext.QueueSet()
        set.setParentPathFile(parentFile)
        set.minIntervalMillisCallbackProcess = 200
        val builder = set.commit()

        urls.forEach {
            val md5Url = com.hao.common.utils.MD5Util.encode(it)
            val fileName = md5Url + suffix
            val localFile = File(parentFile, fileName)
            if (it.isNullOrEmpty() || localFile.isFile && localFile.exists()) {
                LogUtil.i("download-  文件存在，不再下载")
            } else {
                val task = DownloadTask.Builder(it, set.dirUri)
                task.setFilenameFromResponse(false)
                task.setFilename(fileName)
                builder.bind(task)
            }
        }
        builder.setListener(listener)
        return builder.build()
    }

    fun start(context: DownloadContext) {
        if (context.isStarted) {
            return
        }
        context.startOnSerial(object : DownloadListener() {
            override fun taskStart(task: DownloadTask, model: Listener1Assist.Listener1Model) {
                LogUtil.i("download-  taskStart , ${task}")
            }

            override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?, model: Listener1Assist.Listener1Model) {
                LogUtil.i("download-  taskEnd , ${task}   , ${realCause}")
            }
        })
    }

    fun startOnSerial(context: DownloadContext, listener: DownloadListener?) {
        if (context.isStarted) {
            return
        }
        context.startOnSerial(listener)
    }

    fun startOnParallel(context: DownloadContext, listener: DownloadListener?) {
        if (context.isStarted) {
            return
        }
        context.startOnParallel(listener)
    }
}