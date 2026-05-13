package com.hao.ui.widget

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.hao.common.utils.LogUtil

class EditTextLimitLayout : AppCompatEditText {
    constructor(context: Context) : this(context, null){
        LogUtil.e("EditTextLimitLayout1 constructor:")
    }
    constructor(context: Context, attrs: AttributeSet?) : this(context,
        attrs,
        android.R.attr.editTextStyle){
        LogUtil.e("EditTextLimitLayout2 constructor:")
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        LogUtil.e("EditTextLimitLayout3 constructor:")
    }

    fun setMaxLength(max: Int) {
        filters = arrayOf(LengthFilter(max))
    }

    private class LengthFilter(
        /**
         * @return the maximum length enforced by this input filter
         */
        val max: Int,
    ) : InputFilter {
        override fun filter(
            source: CharSequence, start: Int, end: Int, dest: Spanned,
            dstart: Int, dend: Int,
        ): CharSequence? {
            LogUtil.e("dest.toString:" + dest.toString())
            val length = dest.length
            LogUtil.e("dest.length:" + length)
            LogUtil.e("(dend - dstart):" + (dend - dstart))
            var keep = max - (length - (dend - dstart))
            LogUtil.e("keep:" + keep)
            return if (keep <= 0) {
                ""
            } else if (keep >= end - start) {
                null // keep original
            } else {
                keep += start
                if (Character.isHighSurrogate(source[keep - 1])) {
                    --keep
                    if (keep == start) {
                        return ""
                    }
                }
                source.subSequence(start, keep)
            }
        }

    }

}