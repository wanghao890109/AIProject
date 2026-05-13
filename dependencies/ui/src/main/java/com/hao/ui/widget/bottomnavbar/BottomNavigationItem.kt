package com.hao.ui.widget.bottomnavbar

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import java.io.File

class BottomNavigationItem {
    @JvmField
    var icon: Drawable? = null

    /**
     * 默认tab文案
     */
    @JvmField
    var title: String? = null

    /**
     * 选择以后的文案
     */
    @JvmField
    var activeTitle: String? = null

    @JvmField
    var replaceTitle: String? = null

    @ColorInt
    @JvmField
    var activeColor = 0

    @ColorInt
    @JvmField
    var inActiveColor = 0

    /**
     * 需要替换的字段photo
     */
    @JvmField
    var replaceIcon: Drawable? = null

    @JvmField
    var iconSvgaFile: File? = null
    @JvmField
    var iconSvgaAssets: String? = null
    @JvmField
    var svagReplaceFile: File? = null
    @JvmField
    var svgaReplaceAssets: String? = null

    /**
     * 是否需要显示替换资源
     */
    @JvmField
    var isShowReplace = false

    /**
     * 是否有第二状态
     */
    @JvmField
    var hasSecondStatus = false

    /**
     * 是否有转换状态
     */
    @JvmField
    var hasTransformStatus = false

    @JvmField
    var mISlidingTopListener: ISlidingTopListener? = null

    /**
     * @param mIcon  drawable icon for the Tab.
     * @param mTitle title for the Tab.
     */
    constructor(mIcon: Drawable?, title: String) {
        this.icon = mIcon
        this.title = title
    }

    /**
     * @param iconFile resource for the Tab icon.
     * @param title    resource for the title.
     */
    constructor(iconFile: File?, title: String) {
        iconSvgaFile = iconFile
        this.title = title
    }

    /**
     * @param iconFile resource for the Tab icon.
     * @param title    resource for the title.
     */
    constructor(iconFile: File?, title: String, activeTitle: String) {
        iconSvgaFile = iconFile
        this.title = title
        this.activeTitle = activeTitle
    }

    /**
     * @param iconAssetsName resource for the Tab icon.
     * @param title          resource for the title.
     */
    constructor(iconAssetsName: String?, title: String) {
        iconSvgaAssets = iconAssetsName
        this.title = title
    }

    /**
     * @param color active color
     * @return this, to allow builder pattern
     */
    fun setActiveColor(color: Int): BottomNavigationItem {
        activeColor = color
        return this
    }

    /**
     * @param color in-active color
     * @return this, to allow builder pattern
     */
    fun setInActiveColor(color: Int): BottomNavigationItem {
        inActiveColor = color
        return this
    }

    fun setReplaceSvga(replaceSvga: File?, replaceTitle: String?): BottomNavigationItem {
        svagReplaceFile = replaceSvga
        this.replaceTitle = replaceTitle
        return this
    }

    fun setReplaceSvga(replaceAssets: String?, replaceTitle: String?): BottomNavigationItem {
        svgaReplaceAssets = replaceAssets
        this.replaceTitle = replaceTitle
        return this
    }

    fun hasSecondStatus(): Boolean {
        return hasSecondStatus
    }

    fun setHasSecondStatus(hasSecondStatus: Boolean): BottomNavigationItem {
        this.hasSecondStatus = hasSecondStatus
        return this
    }

    fun setHasTransformStatus(hasTransformStatus: Boolean): BottomNavigationItem {
        this.hasTransformStatus = hasTransformStatus
        return this
    }

    /**
     * 点击置顶回调
     */
    interface ISlidingTopListener {
        fun onSlidingTop()
    }

    fun setISlidingTopListener(iSlidingTopListener: ISlidingTopListener?): BottomNavigationItem {
        mISlidingTopListener = iSlidingTopListener
        return this
    }

}