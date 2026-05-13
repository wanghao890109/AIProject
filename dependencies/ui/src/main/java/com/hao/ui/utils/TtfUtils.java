package com.hao.ui.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TtfUtils {
    private TtfUtils() {
    }

    private static class TtfUtilsHoler {
        private static TtfUtils INSTANCE = new TtfUtils();
    }

    public static TtfUtils getInstance() {
        return TtfUtilsHoler.INSTANCE;
    }

    Typeface typeface = null;

    public Typeface getFuturaFont(Context context) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/futura.ttf");
        }
        return typeface;
    }


}
