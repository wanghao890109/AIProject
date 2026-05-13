package com.hao.common.utils

/**
 * Created by wanghao 2020/12/2 15:13
 */
object CompareUtils {

     fun isSameContent(a: String?, b: String?): Boolean {
        if (a == null && b == null) {
            return true
        } else if (a != null && b != null) {
            return a == b
        }
        return false
    }

    fun isSameContent(a: Long?, b: Long?): Boolean {
        if (a == null && b == null) {
            return true
        } else if (a != null && b != null) {
            return a == b
        }
        return false
    }

    fun isSameContent(a: Int?, b: Int?): Boolean {
        if (a == null && b == null) {
            return true
        } else if (a != null && b != null) {
            return a == b
        }
        return false
    }

    fun isSameContent(a: Boolean?, b: Boolean?): Boolean {
        if (a == null && b == null) {
            return true
        } else if (a != null && b != null) {
            return a == b
        }
        return false
    }
}