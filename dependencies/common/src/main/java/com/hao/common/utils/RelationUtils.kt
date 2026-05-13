package com.hao.common.utils


object RelationUtils {
    // 0000000 stranger   ...   000000[Blacked][Black]  00000[Fans][Atten][Friends]
    val friends: Long = 1 shl 0
    val atten: Long = 1 shl 1
    val fans: Long = 1 shl 2

    val black: Long = 1 shl 8
    val blacked: Long = 1 shl 9

    val stranger: Long = 1L shl (56)  //1 0000 0000  0000 0000  0000 0000


//    陌生人                                                                  拉黑          朋友
//    1     0000 0000   0000 0000   0000 0000   0000 0000   0000 0000   0000 0000    0000 0000
    /**
     * 10
     */
    fun isAtten(relation: Long): Boolean {
        LogUtil.e("RelationUtils isAtten:" + relation + " atten:" + atten + " result:" + (atten and relation == atten))
        return (atten and relation) == atten
    }

    fun isFans(relation: Long): Boolean {
        LogUtil.e("RelationUtils isFans:" + relation + " fans:" + fans + " result:" + (fans and relation == fans))
        return (fans and relation) == fans
    }

    fun isFriends(relation: Long): Boolean {
        LogUtil.e("RelationUtils isFriends:" + relation + " friends:" + friends + " result:" + (friends and relation == friends))
        return (friends and relation) == friends
    }

    /**
     * 1 0000 0000
     */
    fun isBlack(relation: Long): Boolean {
        LogUtil.e("RelationUtils isBlack:" + relation + " black:" + black + " result:" + (black and relation == black))
        return (black and relation) == black
    }

    fun isBlacked(relation: Long): Boolean {
        LogUtil.e("RelationUtils isBlacked:" + relation + " blacked:" + blacked + " result:" + (blacked and relation == blacked))
        return (blacked and relation) == blacked
    }

    fun isStranger(relation: Long): Boolean {
        LogUtil.e("RelationUtils isStranger:" + relation + " stranger:" + stranger + " result:" + (stranger and relation == stranger))
        return (stranger and relation) == stranger
    }


}