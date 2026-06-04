package com.ladystoneco.plangym.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.time.LocalDate
import java.util.*

object LocalizationManager {

    /**
     * Converts English digits to Persian digits if current locale is FA.
     */
    fun formatNumbers(input: String, locale: String): String {
        if (locale != "fa") return input
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        return input.map { char ->
            if (char in '0'..'9') persianDigits[char - '0'] else char
        }.joinToString("")
    }

    /**
     * Formats date based on locale (Jalali for FA, Gregorian for EN).
     */
    fun formatDate(date: LocalDate, locale: String): String {
        return if (locale == "fa") {
            val jDate = JalaliCalendar.fromGregorian(date)
            "${formatNumbers(jDate.day.toString(), "fa")} ${JalaliCalendar.getMonthName(jDate.month)} ${formatNumbers(jDate.year.toString(), "fa")}"
        } else {
            "${date.dayOfMonth} ${date.month.name.lowercase().capitalize()} ${date.year}"
        }
    }

    /**
     * Changes app locale at runtime.
     */
    fun setLocale(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}
