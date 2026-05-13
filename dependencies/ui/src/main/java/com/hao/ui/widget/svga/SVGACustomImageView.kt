package com.hao.ui.widget.svga

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import java.net.URL

open class SVGACustomImageView : SVGAImageView {

    var svgaUrl: String? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private fun isSVGAFile(): Boolean {
        return isSVGAFile(svgaUrl)
    }

    open fun isSVGAFile(url: String?): Boolean {
        return url?.endsWith(".svga", true) ?: false
    }

    open fun playSVGA(url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        if (!isSVGAFile(url)) {
            return
        }

        if (this.svgaUrl == url) {

            if (!isAnimating) {
                decodeAndPlay(url)
            }
        } else {
            decodeAndPlay(url)
        }
        this.svgaUrl = url
    }

    open fun stopSVGA() {
        this.svgaUrl = null
        stopAnimation()
    }

    private fun decodeAndPlay(url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        try {
            SVGAParser.Companion.shareParser().decodeFromURL(URL(url), object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    val drawable = SVGADrawable(videoItem)
                    setImageDrawable(drawable)
                    startAnimation()
                }

                override fun onError() {

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}