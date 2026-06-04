package com.ladystoneco.plangym.util

import java.time.LocalDate

/**
 * A lightweight utility for Gregorian-Jalali conversions.
 */
object JalaliCalendar {

    data class JalaliDate(val year: Int, val month: Int, val day: Int)

    fun fromGregorian(date: LocalDate): JalaliDate {
        val gYear = date.year
        val gMonth = date.monthValue
        val gDay = date.dayOfMonth

        val gDaysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gy = gYear - 1600
        var gm = gMonth - 1
        var gd = gDay - 1

        var gDayNo = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400
        for (i in 0 until gm) {
            gDayNo += gDaysInMonth[i + 1]
        }
        if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))) {
            gDayNo++
        }
        gDayNo += gd

        var jDayNo = gDayNo - 79
        val jNp = jDayNo / 12053
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        var jm = 0
        var jd = 0
        if (jDayNo < 186) {
            jm = 1 + jDayNo / 31
            jd = 1 + jDayNo % 31
        } else {
            jm = 7 + (jDayNo - 186) / 30
            jd = 1 + (jDayNo - 186) % 30
        }

        return JalaliDate(jy, jm, jd)
    }

    fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "فروردین"
            2 -> "اردیبهشت"
            3 -> "خرداد"
            4 -> "تیر"
            5 -> "مرداد"
            6 -> "شهریور"
            7 -> "مهر"
            8 -> "آبان"
            9 -> "آذر"
            10 -> "دی"
            11 -> "بهمن"
            12 -> "اسفند"
            else -> ""
        }
    }
}
