package com.hao.core.pagelist

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by wanghao 2021/3/2 16:37
 */
open class PageList<T>(internal val pageSize: Int = PAGE_SIZE) {

    companion object {
        const val REFRESH = 0
        const val LOAD_MORE = 1
        const val PAGE_SIZE = 20
    }

    @IntDef(REFRESH, LOAD_MORE)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Type

    @Type
    internal var type: Int = REFRESH
    internal var pageIndex: Int = 0
    var data: List<T> = emptyList()

    internal fun start(@Type type: Int) {
        if (type != LOAD_MORE) {
            pageIndex = 0
        }
        this.type = type
    }

    internal fun end(data: List<T>) {
        this.data = data
        this.pageIndex += data.size
    }

    fun isNoMoreData(): Boolean {
        return data.size < PAGE_SIZE
    }

    fun isRefresh(): Boolean {
        return type == REFRESH
    }

    fun isEmpty(): Boolean {
        return data.isEmpty()
    }
}