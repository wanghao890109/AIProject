package com.hao.core.pagelist

import androidx.lifecycle.MutableLiveData

class PageListLiveData<T : PageList<DATA>, DATA>(private val pageSize: Int = PageList.PAGE_SIZE) : MutableLiveData<T>() {
    private val pageList: T = PageList<DATA>(pageSize) as T

    fun startLoad(@PageList.Type type: Int) {
        pageList?.start(type)
    }

    fun pageIndex(): Int {
        return pageList.pageIndex
    }

    fun pageSize(): Int {
        return pageList.pageSize
    }

    fun setValue(data: List<DATA>) {
        pageList.end(data)
        value = pageList
    }
}


