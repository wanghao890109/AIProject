package com.hao.common.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by wanghao 2021/4/26 10:39
 */
public class FileSizeUtil {
    public static final long MB = 1024L * 1024L;
    public static final long GB = 1024L * MB;
    public static final long KB_10 = 10L * 1024L;

    /**
     * 把byte大小转成GB 大小
     *
     * @param bytes
     * @return
     */
    public static float bytesToGBytes(long bytes) {
        return bytes / (1024f * 1024f * 1024f);
    }

    /**
     * 把bytes大小转化成MB 大小
     *
     * @param bytes
     * @return
     */
    public static float bytesToMBytes(long bytes) {
        return bytes / (1024f * 1024f);
    }

    /**
     * 把bytes换成kb表示
     *
     * @param bytes
     * @return
     */
    public static float bytesToKBytes(long bytes) {
        return bytes / 1024f;
    }

    /**
     * 保留两位有效数字
     *
     * @param f
     * @return
     */
    private static String trimToTwoNum(float f) {
        double val = Math.round(f * 100) / 100.0;
        return String.valueOf(val);
    }

    public static String getReadableSize(long size) {
        return getReadableSize(size, true);
    }

    private static String getReadableSize(long size, boolean hasFloat) {
        String[] strings = getReadableSizeAndUnit(size, null, hasFloat);
        return strings[0] + " " + strings[1];
    }


    private static String[] getReadableSizeAndUnit(long size, String[] segments, boolean hasFloat) {
        final int readableStringArrayLength = 2;

        if (segments == null) {
            segments = new String[readableStringArrayLength];
        }

        if (size < KB_10) {
            float val = bytesToKBytes(size);
            segments[0] = hasFloat ? trimToTwoNum(val) : String.valueOf((int) val);
            segments[1] = "KB";
        } else if (size < GB) {
            float val = bytesToMBytes(size);
            if (hasFloat) {
                segments[0] = trimToTwoNum(val);
            } else {
                //避免出现0MB
                if (val < 1)
                    val = 1;
                segments[0] = String.valueOf((int) val);
            }
            segments[1] = "MB";
        } else {
            //GB的单位还是要带小数点的
            float val = bytesToGBytes(size);
            segments[0] = trimToTwoNum(val);
            segments[1] = "GB";
        }
        return segments;
    }

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        return formatFileSize(size, 0, true);
    }

    /**
     * 文件大小转换
     *
     * @param number
     * @param pow        保留几位小数
     * @param isContainB 单位中是否带B
     * @return
     */
    public static String formatFileSize(long number, int pow, boolean isContainB) {
        String formatString = "##0";
        if (pow > 0) {
            formatString = formatString + ".";
            for (int i = 0; i < pow; i++) {
                formatString += "0";
            }
        }
        DecimalFormat df = new DecimalFormat(formatString);
        df.setRoundingMode(RoundingMode.DOWN);
        double point = Math.pow(10, pow);
        StringBuilder size = new StringBuilder();
        long oneKB = 1024; // 1KB
        long oneMB = 1024 * oneKB; // 1MB
        long oneGB = 1024 * oneMB;
        if (number <= 0) {
            size.append(isContainB ? "0KB" : "0K");
        } else if (number < oneKB) {
            size.append(isContainB ? "1KB" : "1K");
        } else if (number < oneMB) { // 小于一MB
            double numberDouble = number * 1.0d / oneKB * point / point;
            size.append(df.format(numberDouble)).append(isContainB ? "KB" : "K");
        } else if (number < oneGB) { // 小于一GB
            double numberDouble = number * 1.0d / oneMB * point / point;
            size.append(df.format(numberDouble)).append(isContainB ? "MB" : "M");
        } else {// 大于或者等于一GB
            double numberDouble = number * 1.0d / oneGB * point / point;
            size.append(df.format(numberDouble)).append(isContainB ? "GB" : "G");
        }
        return size.toString();
    }

}
