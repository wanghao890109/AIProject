package com.hao.common.utils

/**
 * Created by wanghao 2021/5/21 11:51
 */
object PhoneNumberUtil {
    fun getDisplayPhoneNo(phoneNo: String): String? {
        val phone = phoneNo.toCharArray()
        for (i in phone.indices) {
            if (i in 3..6) {
                phone[i] = '*'
            }
        }
        return String(phone)
    }
}