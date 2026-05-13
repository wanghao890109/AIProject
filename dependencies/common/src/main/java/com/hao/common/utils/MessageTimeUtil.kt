package com.hao.common.utils

import java.text.SimpleDateFormat
import java.util.*

object MessageTimeUtil {
    var dayNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    fun messageTimeFormat(timesamp: Long): String {
        val timesamp = timesamp * 1000
        var result = ""
        val todayCalendar = Calendar.getInstance()
        val otherCalendar = Calendar.getInstance()
        otherCalendar.timeInMillis = timesamp
        var timeFormat = "M-d HH:mm"
        var yearTimeFormat = "yyyy-M-d HH:mm"
        val yearTemp = todayCalendar[Calendar.YEAR] == otherCalendar[Calendar.YEAR]
        result = if (yearTemp) {
            val todayMonth = todayCalendar[Calendar.MONTH]
            val otherMonth = otherCalendar[Calendar.MONTH]
            if (todayMonth == otherMonth) { //表示是同一个月
                when (todayCalendar[Calendar.DATE] - otherCalendar[Calendar.DATE]) {
                    0 -> getHourAndMin(timesamp)
                    1 -> "Yesterday " + getHourAndMin(timesamp)
                    2, 3, 4, 5, 6 -> {
                        val dayOfMonth = otherCalendar[Calendar.WEEK_OF_MONTH]
                        val todayOfMonth = todayCalendar[Calendar.WEEK_OF_MONTH]
                        if (dayOfMonth == todayOfMonth) { //表示是同一周
                            val dayOfWeek = otherCalendar[Calendar.DAY_OF_WEEK]
                            if (dayOfWeek != 1) { //判断当前是不是星期日   如想显示为：周日 12:09 可去掉此判断
                                dayNames[otherCalendar[Calendar.DAY_OF_WEEK] - 1] + getHourAndMin(timesamp)
                            } else {
                                getTime(timesamp, timeFormat)
                            }
                        } else {
                            getTime(timesamp, timeFormat)
                        }
                    }
                    else -> getTime(timesamp, timeFormat)
                }
            } else {
                getTime(timesamp, timeFormat)
            }
        } else {
            getYearTime(timesamp, yearTimeFormat)
        }
        return result
    }


    fun conversationTimeFormat(timesamp: Long): String {
        val timesamp = timesamp * 1000
        var result = ""
        val todayCalendar = Calendar.getInstance()
        val otherCalendar = Calendar.getInstance()
        otherCalendar.timeInMillis = timesamp
        var timeFormat = "yyyy-M-d HH:mm"
        var yearTimeFormat = "yyyy-M-d HH:mm"

        val yearTemp = todayCalendar[Calendar.YEAR] == otherCalendar[Calendar.YEAR]
        result = if (yearTemp) {
            val todayMonth = todayCalendar[Calendar.MONTH]
            val otherMonth = otherCalendar[Calendar.MONTH]
            if (todayMonth == otherMonth) { //表示是同一个月
                when (todayCalendar[Calendar.DATE] - otherCalendar[Calendar.DATE]) {
                    0 -> getHourAndMin(timesamp)
                    1 -> "Yesterday"
                    2, 3, 4, 5, 6 -> {
                        val dayOfMonth = otherCalendar[Calendar.WEEK_OF_MONTH]
                        val todayOfMonth = todayCalendar[Calendar.WEEK_OF_MONTH]
                        if (dayOfMonth == todayOfMonth) { //表示是同一周
                            dayNames[otherCalendar[Calendar.DAY_OF_WEEK] - 1]
                        } else {
                            getTime(timesamp, timeFormat)
                        }
                    }
                    else -> getTime(timesamp, timeFormat)
                }
            } else {
                getTime(timesamp, timeFormat)
            }
        } else {
            getYearTime(timesamp, yearTimeFormat)
        }
        return result
    }

    /**
     * 是否是同一天
     *
     * @param timestamp 当前消息时间
     * @param lastTimeStamp 上一条消息时间
     * @return 同一天返回 ""  非同一天返回日期 yyyy年MM月dd日
     */
    fun isSameOneDay(timestamp: Long, lastTimeStamp: Long): String {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = timestamp * 1000
        val lastCalendar = Calendar.getInstance()
        lastCalendar.timeInMillis = lastTimeStamp * 1000
        return if (currentCalendar.get(Calendar.YEAR) == lastCalendar.get(Calendar.YEAR)
            && currentCalendar.get(Calendar.MONTH) == lastCalendar.get(Calendar.MONTH)
            && currentCalendar.get(Calendar.DATE) == lastCalendar.get(Calendar.DATE)
        ) {
            ""
        } else {
            DateUtil.toDate(timestamp * 1000, DateUtil.DATE_FORMAT2)
        }
    }

    /**
     * 当天的显示时间格式12小时制
     *
     * @param time
     * @return
     */
    private fun getHourAndMin(time: Long): String {
        val format = SimpleDateFormat("HH:mm")
        var formatStr = format.format(Date(time))
//        if (formatStr.startsWith("0")) {
//            formatStr = formatStr.removePrefix("0")
//        }
        return "$formatStr"
    }

    /**
     * 不同一周的显示时间格式
     *
     * @param time
     * @param timeFormat
     * @return
     */
    private fun getTime(time: Long, timeFormat: String?): String {
        val format = SimpleDateFormat(timeFormat)
        return format.format(Date(time))
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time
     * @param yearTimeFormat
     * @return
     */
    private fun getYearTime(time: Long, yearTimeFormat: String?): String {
        val format = SimpleDateFormat(yearTimeFormat)
        return format.format(Date(time))
    }
}