package com.hao.core.download

import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener1
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist
import java.lang.Exception

/**
 * Created by wanghao 2021/1/26 13:02
 */
abstract class DownloadListener : DownloadListener1() {
    override fun taskStart(task: DownloadTask, model: Listener1Assist.Listener1Model) {

    }

    override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?, model: Listener1Assist.Listener1Model) {

    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {

    }

    override fun connected(task: DownloadTask, blockCount: Int, currentOffset: Long, totalLength: Long) {

    }

    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {

    }
}