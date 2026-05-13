package com.hao.zip


import com.hao.common.utils.*
import com.hao.core.download.DownloadManager
import com.liulishuo.okdownload.DownloadContext
import com.liulishuo.okdownload.DownloadContextListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.hao.service.env.EnvironmentService
import java.io.*
import java.lang.RuntimeException

/**
 * Created by wanghao 2022/8/8
 */
class ZipConfigManager private constructor() {

    companion object {
        private var manager: ZipConfigManager? = null
            get() {
                if (field == null) {
                    field = ZipConfigManager()
                }
                return field
            }

        @Synchronized
        fun get(): ZipConfigManager {
            return manager!!
        }
    }

    fun <K, T : ZipConfigParser<K>> loadConfig(url: String, listener: OnLoadConfigListener<K, T>) {

        val fileName = MD5Util.encode(url)
        val applicationContext = EnvironmentService.getInstance().context
        val zipParentFilePath = applicationContext.filesDir.absolutePath
        val zipFile = File(zipParentFilePath, fileName)
        if (zipFile.exists()) {
            loadLocalConfig(zipFile, listener)
            return
        }

        download(url, listener)
    }

    private fun <K, T : ZipConfigParser<K>> loadLocalConfig(zipFile: File, listener: OnLoadConfigListener<K, T>) {
        val zipParentFilePath = zipFile.parentFile.absolutePath

        if (listener.parser.isExists(zipParentFilePath)) {
           // LogUtil.d("配置文件已存在，不再下载", RuntimeException())
            ThreadPool.getInstance().execute {
                listener?.loadParserSucceed(listener.parser.parser(zipParentFilePath))
            }
        } else {
            ThreadPool.getInstance().execute {
                val configFileParentPath = listener.parser.getConfigFileParentPath(zipParentFilePath)
                DeleteFileUtil.deleteDirectory(configFileParentPath)//删除旧资源
                ZipUtil.unzip(zipFile, zipParentFilePath)
                listener?.loadParserSucceed(listener.parser.parser(zipParentFilePath))
            }
        }
    }

    private fun <K, T : ZipConfigParser<K>> deleteLocalConfig(zipFile: File, listener: OnLoadConfigListener<K, T>) {
        val zipParentFilePath = zipFile.parentFile.absolutePath
        val configFileParentPath = listener.parser.getConfigFileParentPath(zipParentFilePath)
        DeleteFileUtil.deleteDirectory(configFileParentPath)//删除旧资源
    }

    private fun <K, T : ZipConfigParser<K>> download(url: String, listener: OnLoadConfigListener<K, T>) {
        val context = DownloadManager.get().initTasks(arrayListOf(url), object : DownloadContextListener {
            override fun taskEnd(
                context: DownloadContext,
                task: DownloadTask,
                cause: EndCause,
                realCause: java.lang.Exception?,
                remainCount: Int
            ) {
                task.file?.let { taskFile ->

                    deleteLocalConfig(taskFile, listener)

                    loadLocalConfig(taskFile, listener)
                }

                LogUtil.i("download-  taskEnd , ${task.filename}")
            }

            override fun queueEnd(context: DownloadContext) {
                LogUtil.i("download-  queueEnd , ${context.tasks.size}")
            }
        })
        DownloadManager.get().start(context)
    }

    abstract class OnLoadConfigListener<K, T : ZipConfigParser<K>>(val parser: T) {
        abstract fun loadParserSucceed(list: MutableList<K>)
    }

}