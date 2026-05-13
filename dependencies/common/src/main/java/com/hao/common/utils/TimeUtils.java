package com.hao.common.utils;

/**
 * Created by wanghao2 on 2017/8/17.
 */

public class TimeUtils {

    public static String getMusicTime(long time) {
        return getMusicTime((int) time);
    }

    public static String getMusicTime(int time) {
        StringBuilder softReference = new StringBuilder();
        if (time == 0) {
            return "00:00";
        }
        if (time < 60) {
            softReference.append("00:");
            if (time < 10) {
                softReference.append("0");
                softReference.append(time);
            } else {
                softReference.append(time);
            }

        } else {
            int minute = time / 60;
            int second = time % 60;
            if (minute >= 10) {
                softReference.append(minute);
                softReference.append(":");
            } else {
                softReference.append("0");
                softReference.append(minute);
                softReference.append(":");
            }
            if (second >= 10) {
                softReference.append(second);
            } else {
                softReference.append("0");
                softReference.append(second);
            }
        }
        return softReference.toString();
    }

    public static String getClockTime(int time) {
        StringBuilder softReference = new StringBuilder();
        if (time <= 0) {
            return "00:00";
        }
        if (time < 60) {
            softReference.append("00:");
            if (time < 10) {
                softReference.append("0");
                softReference.append(time);
            } else {
                softReference.append(time);
            }

        } else {
            int minute = time / 60;
            int second = time % 60;
            if (minute >= 10) {
                softReference.append(minute);
                softReference.append(":");
            } else {
                softReference.append("0");
                softReference.append(minute);
                softReference.append(":");
            }
            if (second >= 10) {
                softReference.append(second);
            } else {
                softReference.append("0");
                softReference.append(second);
            }
        }
        return softReference.toString();
    }

}
