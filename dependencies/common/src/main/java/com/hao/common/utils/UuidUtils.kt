package com.hao.common.utils

import android.content.Context
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import com.hao.common.cache.SPCacheUtil
import java.util.*
import java.util.regex.Pattern

/**
 * Created by wanghao 2022/10/14
 */
object UuidUtils {

    private var spCacheUtil: SPCacheUtil? = null

    private fun getSpCacheUtil(context: Context): SPCacheUtil {
        if (spCacheUtil == null) {
            spCacheUtil = SPCacheUtil(context, "uuid_utils")
        }
        return spCacheUtil!!
    }

    private val uuidPath = Environment.getExternalStorageDirectory().absolutePath + "/tencent/.u/u"
    private val spKey = "uuid"

    private var sCacheUuid: String? = null

    private var sAndroidId: String? = null

    private fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver, Settings.Secure.ANDROID_ID
        ) ?: ""
    }

    fun getUuid(context: Context): String? {
        if (sCacheUuid != null) {
            return sCacheUuid
        }
        try {
            val spCacheUtil = getSpCacheUtil(context)
            synchronized(UuidUtils::class.java) {
                val shareUuid: String = spCacheUtil.get(spKey, null)
                if (!TextUtils.isEmpty(shareUuid)) {
                    sCacheUuid = shareUuid
                    FileUtil.saveString2File(uuidPath, sCacheUuid) //至少进程初始化的时候，发现有权限，那么就去存一下，还行
                    return sCacheUuid
                }

                var fileUuid: String = FileUtil.readStringFromFile(uuidPath)
                if (!TextUtils.isEmpty(fileUuid)) {
                    LogUtil.i(" fileUuid 1:$fileUuid")
                    fileUuid = replace(fileUuid)
                    LogUtil.i("fileUuid 2:$fileUuid")

                    spCacheUtil.put(spKey, fileUuid)
                    sCacheUuid = fileUuid
                    return sCacheUuid
                }
                var androidId = getAndroidId(context)
                if (TextUtils.isEmpty(androidId) || "9774d56d682e549c" == androidId) {
                    androidId = getRandomString(16)
                }
                FileUtil.saveString2File(uuidPath, androidId)

                spCacheUtil.put(spKey, fileUuid)

                sCacheUuid = androidId
            }
        } catch (e: Exception) {
            sCacheUuid = if (TextUtils.isEmpty(sAndroidId)) {
                sAndroidId = getAndroidId(context)
                sAndroidId
            } else {
                sAndroidId
            }
        }
        return sCacheUuid
    }

    fun replace(str: String): String {
        var destination = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            destination = m.replaceAll("")
        }
        return destination
    }

    fun getRandomString(length: Int): String {
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        val str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890"
        //由Random生成随机数
        val random = Random()
        val sb = StringBuffer()
        //长度为几就循环几次
        for (i in 0 until length) {
            //产生0-61的数字
            val number = random.nextInt(62)
            //将产生的数字通过length次承载到sb中
            sb.append(str[number])
        }
        //将承载的字符转换成字符串
        return sb.toString()
    }
}