package com.hao.common.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ConvertUtils {

    private static final int ZERO = 0;
    private static final float ZERO_F = 0.0f;

    public static int toInt(String num) {
        try {
            if (TextUtils.isEmpty(num)) {
                return ZERO;
            }

            String point = ".";
            if (!num.contains(point)) {
                return Integer.valueOf(num);
            }

            String target = num.substring(0, num.indexOf(point));
            if (TextUtils.isEmpty(target)) {
                return ZERO;
            }
            String point2 = ",";
            if (target.contains(point2)) {
                target = target.replace(",", "");
            }
            return Integer.valueOf(target);

        } catch (Exception e) {
        }

        return ZERO;
    }

    public static float toFloat(String num) {
        try {
            if (TextUtils.isEmpty(num)) {
                return ZERO_F;
            }

            return Float.valueOf(num);
        } catch (Exception e) {
        }

        return ZERO_F;
    }

    public static long toLong(String num) {
        try {
            if (TextUtils.isEmpty(num)) {
                return ZERO;
            }

            String point = ".";
            if (!num.contains(point)) {
                return Long.valueOf(num);
            }

            String target = num.substring(0, num.indexOf(point));
            if (TextUtils.isEmpty(target)) {
                return ZERO;
            }

            return Long.valueOf(target);

        } catch (Exception e) {
        }

        return ZERO;
    }

    public static double toDouble(String num) {
        try {
            if (TextUtils.isEmpty(num)) {
                return ZERO;
            }

            return Double.valueOf(num);
        } catch (Exception e) {
        }

        return ZERO;
    }

    public static String toString(Double num) {
        try {
            if (num == null) {
                return "";
            }

            return String.valueOf(num);
        } catch (Exception e) {
        }

        return "";
    }

    public static String toString(float num) {
        try {
            return String.valueOf(num);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String toString(Integer num) {
        try {
            if (num == null) {
                return "";
            }

            return String.valueOf(num);
        } catch (Exception e) {
        }

        return "";
    }

    public static String toString(Long num) {
        try {
            if (num == null) {
                return "";
            }

            return String.valueOf(num);
        } catch (Exception e) {
        }

        return "";
    }

    public static int[] toIntArray(String[] strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        int[] arr = new int[strings.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = toInt(strings[i].replace(" ", ""));
        }
        return arr;
    }

    /**
     * 过滤掉小数点后面的0
     * 1.0 -> 1
     * 10.0 -> 10
     * 1.01 -> 1.01
     */
    public static String formatNoZero(double num) {
        NumberFormat nf = new DecimalFormat("#.##");
        return nf.format(num);
    }

    public static String formatTwoDecimalPoint(double num) {
        NumberFormat nf = new DecimalFormat("0.00");
        return nf.format(num);
    }

    public static String formatMoney(double num) {
        NumberFormat nf = new DecimalFormat("#,##0.00");
        return nf.format(num);
    }

    public static long toLong(String number, long defaultValue) {
        long value = defaultValue;

        if (TextUtils.isEmpty(number)) {
            return value;
        }

        try {
            value = Long.valueOf(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static int toInt(String number, int defaultValue) {
        int value = defaultValue;

        if (TextUtils.isEmpty(number)) {
            return value;
        }

        try {
            value = Integer.valueOf(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return value;
    }
}
