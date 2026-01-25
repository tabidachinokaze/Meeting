package moe.tabidachi.meeting.utils

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import java.util.Locale

object TimeFormat {
    val MONTH_DAY_FORMAT_ENGLISH = LocalDate.Format {
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        day()
    }

    val WEEK_FORMAT_ENGLISH = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
    }

    val MONTH_DAY_WEEK_FORMAT_ENGLISH = LocalDate.Format {
        date(WEEK_FORMAT_ENGLISH)
        chars(", ")
        date(MONTH_DAY_FORMAT_ENGLISH)
    }

    private val DAY_OF_WEEK_NAMES_CHINESE = DayOfWeekNames(
        monday = "星期一",
        tuesday = "星期二",
        wednesday = "星期三",
        thursday = "星期四",
        friday = "星期五",
        saturday = "星期六",
        sunday = "星期日"
    )

    val MONTH_DAY_FORMAT_CHINESE = LocalDate.Format {
        monthNumber(padding = Padding.NONE)
        char('月')
        day()
        char('日')
    }

    val WEEK_FORMAT_CHINESE = LocalDate.Format {
        dayOfWeek(DAY_OF_WEEK_NAMES_CHINESE)
    }

    val MONTH_DAY_WEEK_FORMAT_CHINESE = LocalDate.Format {
        date(MONTH_DAY_FORMAT_CHINESE)
        char('，')
        date(WEEK_FORMAT_CHINESE)
    }

    val HOUR_MINUTE_12H_FORMAT_ENGLISH = LocalTime.Format {
        amPmHour(padding = Padding.NONE)
        char(':')
        minute()
        char(' ')
        amPmMarker("AM", "PM")
    }

    val HOUR_MINUTE_12H_FORMAT_CHINESE = LocalTime.Format {
        amPmMarker("上午", "下午")
        amPmHour(padding = Padding.NONE)
        char(':')
        minute()
    }

    val HOUR_MINUTE_24H_FORMAT = LocalTime.Format {
        hour(padding = Padding.NONE)
        char(':')
        minute()
    }

    val MONTH_DAY_HOUR_MINUTE_12H_FORMAT_ENGLISH = LocalDateTime.Format {
        date(MONTH_DAY_FORMAT_ENGLISH)
        chars(", ")
        time(HOUR_MINUTE_12H_FORMAT_ENGLISH)
    }

    val MONTH_DAY_HOUR_MINUTE_24H_FORMAT_ENGLISH = LocalDateTime.Format {
        date(MONTH_DAY_FORMAT_ENGLISH)
        chars(", ")
        time(HOUR_MINUTE_24H_FORMAT)
    }

    val MONTH_DAY_HOUR_MINUTE_12H_FORMAT_CHINESE = LocalDateTime.Format {
        date(MONTH_DAY_FORMAT_CHINESE)
        char('，')
        time(HOUR_MINUTE_12H_FORMAT_CHINESE)
    }

    val MONTH_DAY_HOUR_MINUTE_24H_FORMAT_CHINESE = LocalDateTime.Format {
        date(MONTH_DAY_FORMAT_CHINESE)
        char('，')
        time(HOUR_MINUTE_24H_FORMAT)
    }

    fun getAutoTimeFormat(isToday: Boolean, is24HourFormat: Boolean, locale: Locale) =
        LocalDateTime.Format {
            when (isToday) {
                true -> when (is24HourFormat) {
                    true -> when (locale) {
                        Locale.CHINESE -> time(HOUR_MINUTE_24H_FORMAT)
                        else -> time(HOUR_MINUTE_24H_FORMAT)
                    }

                    false -> when (locale) {
                        Locale.CHINESE -> time(HOUR_MINUTE_12H_FORMAT_CHINESE)
                        else -> time(HOUR_MINUTE_12H_FORMAT_ENGLISH)
                    }
                }

                false -> when (is24HourFormat) {
                    true -> when (locale) {
                        Locale.CHINESE -> dateTime(MONTH_DAY_HOUR_MINUTE_24H_FORMAT_CHINESE)
                        else -> dateTime(MONTH_DAY_HOUR_MINUTE_24H_FORMAT_ENGLISH)
                    }

                    false -> when (locale) {
                        Locale.CHINESE -> dateTime(MONTH_DAY_HOUR_MINUTE_12H_FORMAT_CHINESE)
                        else -> dateTime(MONTH_DAY_HOUR_MINUTE_12H_FORMAT_ENGLISH)
                    }
                }
            }
        }
}

val is24HourFormat: Boolean
    @Composable @ReadOnlyComposable get() = DateFormat.is24HourFormat(LocalContext.current)
