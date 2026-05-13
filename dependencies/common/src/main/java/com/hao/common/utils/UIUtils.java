package com.hao.common.utils;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.AttrRes;
import androidx.annotation.StringRes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by yaocheng on 2017/6/23.
 */

public class UIUtils {
    private final static Rect sRect = new Rect();
    public static int PAGE_SIZE = 20; // 每页加载数量
    public static boolean isSoftKeyBoardShow(View v) {
        final int screenHeight = v.getRootView().getHeight();
        v.getWindowVisibleDisplayFrame(sRect);
        final int heightDifference = screenHeight - sRect.bottom;
        if (heightDifference > screenHeight / 4) {
            return true;
        }
        return false;
    }

    public static int getThemeColor(Context c, @AttrRes int themeColor) {

        TypedArray array = c.getTheme().obtainStyledAttributes(new int[]{
                themeColor,
        });
        int mAccentColor = array.getColor(0, 0);

        array.recycle();

        return mAccentColor;
    }

    public static Drawable getThemeDrawable(Context c, @AttrRes int themeDrawable) {

        TypedArray array = c.getTheme().obtainStyledAttributes(new int[]{
                themeDrawable,
        });
        Drawable d = array.getDrawable(0);
        array.recycle();

        return d;
    }

    public static int getThemeDimension(Context c, @AttrRes int themeDimen) {

        TypedArray array = c.getTheme().obtainStyledAttributes(new int[]{
                themeDimen,
        });
        int size = array.getDimensionPixelOffset(0, 0);
        array.recycle();

        return size;
    }

    public static float getX(View v) {
        if (v != null) {
            return v.getLeft() + getParentX(v.getParent());
        }
        return 0;
    }

    public static float getParentX(ViewParent parent) {
        if (parent != null && parent instanceof ViewGroup) {
            return ((ViewGroup) parent).getLeft() + getParentX(parent.getParent());
        }
        return 0;
    }

    public static float getY(View v) {
        if (v != null) {
            return v.getTop() + getParentY(v.getParent());
        }
        return 0;
    }

    private static float getParentY(ViewParent parent) {
        if (parent != null && parent instanceof ViewGroup) {
            return ((ViewGroup) parent).getTop() + getParentY(parent.getParent());
        }
        return 0;
    }


    public static int dip2px(Context context, int dpValue) {
        return dip2px(context, (double) dpValue);
    }

    public static int dip2px(Context context, double dpValue) {
        if (context == null) {
            return 0;
        } else if (context.getResources() == null) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    public static int px2dip(Context context, float pxValue) {
        float desity = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / desity + 0.5);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        } else if (context.getResources() == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        } else if (context.getResources() == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return 0;
        } else if (context.getResources() == null) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class<? extends Window> clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, dark ? darkModeFlag : 0, darkModeFlag);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param resStrId
     * @param color
     * @param object
     * @return
     */
    public static SpannableStringBuilder getSpannableStringBuilder(Context context, int resStrId, int color,
                                                                   String... object) {
        return getSpannableStyleStringBuilder(context, resStrId, color, Typeface.NORMAL, object);
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param resStrId
     * @param color
     * @param object
     * @param style
     * @return
     */
    public static SpannableStringBuilder getSpannableStyleStringBuilder(Context context, @StringRes int resStrId, int color,
                                                                        int style, String... object) {
        if (context == null || context.getResources() == null) {
            return null;
        }
        String values = context.getString(resStrId, (Object[]) object);
        if (values == null) {
            return new SpannableStringBuilder("");
        }
        SpannableStringBuilder processSpanStr = new SpannableStringBuilder(values);
        try {
            int len = object.length;
            int index[] = new int[len];
            for (int i = 0; i < len; i++) {
                index[i] = values.indexOf(object[i]);
            }

            for (int i = 0; i < len; i++) {
                index[i] = values.indexOf(object[i]);
                processSpanStr.setSpan(new ForegroundColorSpan(color), index[i],
                        index[i] + object[i].length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                processSpanStr.setSpan(new StyleSpan(style), index[i], index[i] + object[i].length(),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        } catch (Exception e) {
            return new SpannableStringBuilder("");
        }

        return processSpanStr;
    }

    public static SpannableStringBuilder getSpannableStyleStringBuilder(String content, int color, String... keys) {
        String values = content;
        if (values == null) {
            return new SpannableStringBuilder("");
        }
        SpannableStringBuilder processSpanStr = new SpannableStringBuilder(values);
        try {
            int len = keys.length;
            int index[] = new int[len];
            for (int i = 0; i < len; i++) {
                index[i] = values.indexOf(keys[i]);
            }

            for (int i = 0; i < len; i++) {
                index[i] = values.indexOf(keys[i]);
                processSpanStr.setSpan(new ForegroundColorSpan(color), index[i],
                        index[i] + keys[i].length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                processSpanStr.setSpan(new StyleSpan(Typeface.NORMAL), index[i], index[i] + keys[i].length(),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        } catch (Exception e) {
            return new SpannableStringBuilder(values);
        }

        return processSpanStr;
    }

    /**
     * 给Image添加光晕
     *
     * @param context       上下文
     * @param imageId       图片id
     * @param shadowColorId 光晕颜色id
     * @param radius        （外围光晕宽度，也可以根据图片尺寸按照比例来，根据实际需求）
     * @return 加完光晕的图片
     */
    public static Bitmap addHaloToImage(Context context, int imageId, int shadowColorId, float radius) {
        BitmapDrawable mBitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(imageId);
        Bitmap mBitmap = mBitmapDrawable.getBitmap();
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int shadowRadius = (int) (getDensity(context) * radius);
        //创建一个比原来图片大2个radius的图片对象
        Bitmap mHaloBitmap = Bitmap.createBitmap(mBitmapWidth + shadowRadius * 2, mBitmapHeight + shadowRadius * 2, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mHaloBitmap);
        //设置抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(context.getResources().getColor(shadowColorId));
        //外发光
        mPaint.setMaskFilter(new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.OUTER));
        //从原位图中提取只包含alpha的位图
        Bitmap alphaBitmap = mBitmap.extractAlpha();
        //在画布上（mHaloBitmap）绘制alpha位图
        mCanvas.drawBitmap(alphaBitmap, shadowRadius, shadowRadius, mPaint);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mCanvas.drawBitmap(mBitmap, null, new Rect(shadowRadius + 1, shadowRadius + 1, shadowRadius + mBitmapWidth - 1, shadowRadius + mBitmapHeight - 1), null);
        //回收
        mBitmap.recycle();
        alphaBitmap.recycle();
        return mHaloBitmap;
    }
    public static Boolean copyStr(Context context, String copyStr) {
        try {
            //获取剪贴板管理器
            if (null == context)
                return false;
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //    获取px和dp转化倍数值
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

}
