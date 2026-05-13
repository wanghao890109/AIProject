package com.hao.common.utils

import android.content.Context
import android.graphics.Typeface

object TypeFaceUtil {

    fun typeFace(context: Context): Typeface {
        return Typeface.createFromAsset(
            context?.assets,
            "fonts/LEMON MILK Bold.ttf"
        )
    }
}