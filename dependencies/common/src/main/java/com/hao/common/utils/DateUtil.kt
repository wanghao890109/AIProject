package com.hao.common.utils

import android.content.Context
import android.text.TextUtils
import com.hao.common.R
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.JvmOverloads

/**
 * 日期时间工具类
 */
object DateUtil {
    /**
     * 时:分
     */
    val DATE_FORMAT0 = SimpleDateFormat(
        "HH:mm", Locale.getDefault()
    )

    /**
     * 年-月-日 时:分:秒
     */
    val DATE_FORMAT = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
    )

    /**
     * 年-月-日
     */
    val DATE_FORMAT2 = SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault()
    )

    /**
     * 年-月-日 时:分
     */
    val DATE_FORMAT3 = SimpleDateFormat(
        "yyyy-MM-dd HH:mm", Locale.getDefault()
    )

    /**
     * 分:秒
     */
    val DATE_FORMAT4 = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    )

    /**
     * x月x日
     */
    val DATE_FORMAT5 = SimpleDateFormat(
        "M月d日", Locale.getDefault()
    )

    /**
     * 年-月-日
     */
    val DATE_FORMAT6 = SimpleDateFormat(
        "yyyy.MM.dd", Locale.getDefault()
    )

    val DATE_FORMAT7 = SimpleDateFormat(
        "yyyy年MM月dd日", Locale.getDefault()
    )

    fun yyyyMMDDHHMMSS(millisecondDate: Long): String {
        return toDate(millisecondDate, DATE_FORMAT)
    }

    fun yyyyMMDDHHMM(millisecondDate: Long): String {
        return toDate(millisecondDate, DATE_FORMAT3)
    }

    fun yyyyMMDD(millisecondDate: Long): String {
        return toDate(millisecondDate, DATE_FORMAT2)
    }

    /**
     * 将毫秒级整数转换为字符串格式时间
     *
     * @param millisecondDate 毫秒级时间整数
     * @param format          要转换成的时间格式(参见 DateUtil常量)
     * @return 返回相应格式的时间字符串
     */
    fun toDate(millisecondDate: Long, format: SimpleDateFormat): String {
        var time = ""
        try {
            time = format.format(Date(millisecondDate))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return time
    }

    fun stringToLongMs(date: String?): Long {
        if (!TextUtils.isEmpty(date)) {
            try {
                val seconds = Calendar.getInstance()
                seconds.time = DATE_FORMAT4.parse(date)
                return seconds[Calendar.SECOND].toLong()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return 0
    }

    fun stringToLong(date: String?): Long {
        if (!TextUtils.isEmpty(date)) {
            try {
                return DATE_FORMAT.parse(date).time / 1000
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return 0
    }
    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    /**
     * 格式化时间
     *
     * @param time 不显示HH:mm，并且不显示“今天”
     * @return
     */
    @JvmOverloads
    fun formatDateTime(time: String?, showHours: Boolean = false, showToday: String = ""): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (time == null || "" == time || time.length < 19) {
            return ""
        }
        var date: Date? = null
        try {
            date = format.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val current = Calendar.getInstance()
        val today = Calendar.getInstance() //今天
        today[Calendar.YEAR] = current[Calendar.YEAR]
        today[Calendar.MONTH] = current[Calendar.MONTH]
        today[Calendar.DAY_OF_MONTH] = current[Calendar.DAY_OF_MONTH]
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today[Calendar.HOUR_OF_DAY] = 0
        today[Calendar.MINUTE] = 0
        today[Calendar.SECOND] = 0
        val yesterday = Calendar.getInstance() //昨天
        yesterday[Calendar.YEAR] = current[Calendar.YEAR]
        yesterday[Calendar.MONTH] = current[Calendar.MONTH]
        yesterday[Calendar.DAY_OF_MONTH] = current[Calendar.DAY_OF_MONTH] - 1
        yesterday[Calendar.HOUR_OF_DAY] = 0
        yesterday[Calendar.MINUTE] = 0
        yesterday[Calendar.SECOND] = 0
        if (date != null) {
            current.time = date
        }
        return if (current.after(today)) {
            showToday + " " + time.split(" ").toTypedArray()[1].substring(0, 5)
        } else {
            val index = time.indexOf("-") + 1
            if (showHours) {
                time.substring(index, time.length).substring(0, 11)
            } else {
                time.substring(index, time.length).substring(0, 5)
            }
        }
    }

    /**
     * 将时间戳格式化
     *
     * @param seconds
     * @return
     */
    fun timeStamp2Date(seconds: String?, format: String?): String {
        var format = format
        if (seconds == null || TextUtils.isEmpty(seconds) || seconds == "null") {
            return ""
        }
        if (format == null || TextUtils.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(java.lang.Long.valueOf(seconds + "000")))
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    val currentTime: String
        get() = toDate(System.currentTimeMillis(), DATE_FORMAT)

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    fun getStandardDate(timeStr: String): String {
        val sb = StringBuffer()
        val t = timeStr.toLong()
        val time = System.currentTimeMillis() - t * 1000
        val mill = Math.ceil((time / 1000).toDouble()).toLong() //秒前
        val minute = Math.ceil((time / 60 / 1000.0f).toDouble()).toLong() // 分钟前
        val hour = Math.ceil((time / 60 / 60 / 1000.0f).toDouble()).toLong() // 小时
        val day = Math.ceil((time / 24 / 60 / 60 / 1000.0f).toDouble()).toLong() // 天前
        if (day > 7) {
            sb.append(timeStamp2Date(timeStr, "yyyy-MM-dd"))
            return sb.toString()
        } else if (day > 1 && day <= 7) {
            sb.append(day.toString() + "天")
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天")
            } else {
                sb.append(hour.toString() + "小时")
            }
        } else if (minute - 1 > 0) {
            if (minute == 60L) {
                sb.append("1小时")
            } else {
                sb.append(minute.toString() + "分钟")
            }
        } else if (mill - 1 > 0) {
            if (mill == 60L) {
                sb.append("1分钟")
            } else {
                sb.append(mill.toString() + "秒")
            }
        } else {
            sb.append("刚刚")
        }
        if (sb.toString() != "刚刚") {
            sb.append("前")
        }
        return sb.toString()
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param second 时间戳 秒
     * @return
     */
    fun getStandardDate(second: Long): String {
        val millisecond = second * 1000 //转成毫秒
        val sb = StringBuffer()
        val time = System.currentTimeMillis() - millisecond
        val mill = Math.ceil((time / 1000).toDouble()).toLong() //秒前
        val minute = Math.ceil((time / 60 / 1000.0f).toDouble()).toLong() // 分钟前
        val hour = Math.ceil((time / 60 / 60 / 1000.0f).toDouble()).toLong() // 小时
        val day = Math.ceil((time / 24 / 60 / 60 / 1000.0f).toDouble()).toLong() // 天前
        if (day > 7) {
            sb.append(toDate(millisecond, DATE_FORMAT2))
            return sb.toString()
        } else if (day > 1 && day <= 7) {
            sb.append(day.toString() + "天")
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天")
            } else {
                sb.append(hour.toString() + "小时")
            }
        } else if (minute - 1 > 0) {
            if (minute == 60L) {
                sb.append("1小时")
            } else {
                sb.append(minute.toString() + "分钟")
            }
        } else if (mill - 1 > 0) {
            if (mill == 60L) {
                sb.append("1分钟")
            } else {
                sb.append(mill.toString() + "秒")
            }
        } else {
            sb.append("刚刚")
        }
        if (sb.toString() != "刚刚") {
            sb.append("前")
        }
        return sb.toString()
    }

    fun timeStamp2Date(timestamp: Long): String {
        val cal1 = Calendar.getInstance()
        cal1.time = Date(timestamp)
        val cal2 = Calendar.getInstance()
        cal2.time = Date()
        val preYear = cal1[Calendar.YEAR] < cal2[Calendar.YEAR]
        return if (preYear) {
            //如果不是在一年以内,则弹出提示
            SimpleDateFormat("yyyy年MM月dd日").format(timestamp)
        } else {
            //在一年以内做的逻辑
            if (isToday(Date(timestamp))) {
                //今天
                SimpleDateFormat("HH:mm").format(timestamp)
            } else {
                SimpleDateFormat("MM月dd日").format(timestamp)
            }
        }
    }

    fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        require(!(cal1 == null || cal2 == null)) { "The dates must not be null" }
        return cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    fun isToday(date: Date?): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    fun getPublishTime(context: Context, publishTime: Long): String {
        LogUtil.e("DateUtil currentTimeMillis=" + System.currentTimeMillis() + "publishTime * 1000=" + publishTime * 1000)
        LogUtil.e("DateUtil " + (System.currentTimeMillis() - publishTime * 1000))
        val currentTime = Calendar.getInstance()
        val birthdayTime = Calendar.getInstance()
        birthdayTime.timeInMillis = publishTime * 1000

        var year = currentTime.get(Calendar.YEAR) - birthdayTime.get(Calendar.YEAR)
        if (year > 0) {
            LogUtil.e("DateUtil year=$year")
            return String.format(
                context.getString(R.string.years_ago), year
            )
        }
        val months = currentTime.get(Calendar.MONTH) - birthdayTime.get(Calendar.MONTH)
        if (months > 0) {
            LogUtil.e("DateUtil months=$months")
            return String.format(
                context.getString(R.string.months_ago), months
            )
        }
        val week = currentTime.get(Calendar.WEEK_OF_MONTH) - birthdayTime.get(Calendar.WEEK_OF_MONTH)
        if (week > 0) {
            LogUtil.e("DateUtil week=$week")
            return String.format(
                context.getString(R.string.weeks_ago), week
            )
        }
        val day = currentTime.get(Calendar.DAY_OF_MONTH) - birthdayTime.get(Calendar.DAY_OF_MONTH)
        if (day > 0) {
            LogUtil.e("DateUtil day=$day")
            return String.format(
                context.getString(R.string.days_ago), day
            )
        }
        val hour = currentTime.get(Calendar.HOUR_OF_DAY) - birthdayTime.get(Calendar.HOUR_OF_DAY)
        if (hour > 0) {
            LogUtil.e("DateUtil hour=$hour")
            return String.format(
                context.getString(R.string.hours_ago), hour
            )
        }
        val minute = currentTime.get(Calendar.MINUTE) - birthdayTime.get(Calendar.MINUTE)
        return if (minute > 0) {
            LogUtil.e("DateUtil minute=$minute")
            String.format(
                context.getString(R.string.minutes_ago), minute
            )
        } else {
            LogUtil.e("DateUtil now")
            context.getString(R.string.now)
        }

    }

    fun getUploadTime(context: Context, publishTime: Long): String {
        LogUtil.e("getUploadTime publishTime " + (publishTime * 1000))
        var time = (System.currentTimeMillis() - publishTime * 1000)
        LogUtil.e("getUploadTime " + (System.currentTimeMillis() - publishTime * 1000))
        val currentTime = Calendar.getInstance()
        val birthdayTime = Calendar.getInstance()
        birthdayTime.timeInMillis = publishTime * 1000

        val hour = time / (60 * 60 * 1000)
        val minute = time / (60 * 1000)
        LogUtil.e("getUploadTime hour=$hour")
        if (hour > 0) {
            return String.format(
                context.getString(R.string.hours), hour
            )
        }
        LogUtil.e("getUploadTime minute=$minute")
        return if (minute > 0) {
            String.format(
                context.getString(R.string.minutes), minute
            )
        } else {
            LogUtil.e("getUploadTime now")
            ""
        }

    }

    /**
     * 传递过来是秒
     */
    fun birthdayToAgeSecond(birthdaySecond: Long): Int {
        if (birthdaySecond > 0) {
            val currentTime = Calendar.getInstance()
            val birthdayTime = Calendar.getInstance()
            birthdayTime.timeInMillis = birthdaySecond * 1000
            var year = currentTime.get(Calendar.YEAR) - birthdayTime.get(Calendar.YEAR)
            val months = currentTime.get(Calendar.MONTH) - birthdayTime.get(Calendar.MONTH)
            val day = currentTime.get(Calendar.DAY_OF_MONTH) - birthdayTime.get(Calendar.DAY_OF_MONTH)
            //  val day = currentTime.day - birthdayTime.date
//            if (months < 0 || months == 0) {
//                if (year > 0) {
//                    year -= 1
//                } else {
//                    return 0
//                }
//            }
            if (months == 0) {
                //当月 还没到生日那天
                if (day < 0) {
                    if (year > 0) {
                        year -= 1
                    } else {
                        return 0
                    }
                }
            } else if (months < 0) {
                //小于当月，不足一年，减一岁
                if (year > 0) {
                    year -= 1
                } else {
                    return 0
                }
            }
            return year
        }
        return 0
    }

    /**
     * 传入的秒,显示年月日
     */
    fun dateFormatSecond(currentLanguage: String, createdAtTimeSecond: Long): String {
        return dateFormatMillis(currentLanguage, createdAtTimeSecond * 1000, false)
    }

    fun dateFormatMessageTime(context: Context, timestamp: Long): String {
        return dateFormatSecond(context.getResources().getConfiguration().locale.language, timestamp)
    }

    private fun dateFormatMillis(currentLanguage: String, createdAtTime: Long, isHaveHours: Boolean = true): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = createdAtTime
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH) + 1
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        LogUtil.e("createdAt year = $year")
        LogUtil.e("createdAt month = $month")
        LogUtil.e("createdAt day = $day")
        var mDate = Date(createdAtTime)
        var dateFormat: DateFormat? = null
//        var zhDateString = "$year/$month/$day$hoursAndMinute"
//        var enDateString = "$day/$month/$year$hoursAndMinute"
        var formateZh = if (isHaveHours) {
            "yyyy/MM/d HH:mm"
        } else {
            "yyyy/MM/d"
        }
        var formateEn = if (isHaveHours) {
            "d/MM/yyyy HH:mm"
        } else {
            "d/MM/yyyy"
        }
        when (currentLanguage) {
            Locale.CHINESE.language,
                -> {
                dateFormat = SimpleDateFormat(formateZh, Locale.CHINESE)
            }

            Locale.ENGLISH.language -> {
                dateFormat = SimpleDateFormat(formateEn, Locale.ENGLISH)
            }

            else -> {
                dateFormat = SimpleDateFormat(formateEn, Locale.ENGLISH)
            }
        }
        return dateFormat.format(mDate)

    }

}