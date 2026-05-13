package com.hao.zip

import com.hao.common.utils.LogUtil
import java.io.File

/**
 * Created by wanghao 2022/8/8
 */
open abstract class ZipConfigParser<T>(val configParentFolder: String, private val configFileName: String) {

    abstract fun parser(zipParentFilePath: String): MutableList<T>

    fun isExists(zipParentFilePath: String): Boolean {
        val configFile = configFile(zipParentFilePath)
        if (configFile.exists()) {
            LogUtil.i("配置文件存在")
            return true
        }
        return false
    }

    fun configFile(zipParentFilePath: String): File {
        return File(getConfigFileParentPath(zipParentFilePath) + File.separator + configFileName)
    }

    fun getConfigFileParentPath(zipParentFilePath: String): String {
        return zipParentFilePath + File.separator + configParentFolder
    }
}