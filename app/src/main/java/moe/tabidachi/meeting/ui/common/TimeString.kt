package moe.tabidachi.meeting.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import moe.tabidachi.meeting.utils.TimeFormat
import moe.tabidachi.meeting.utils.is24HourFormat
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
@ReadOnlyComposable
fun monthDayWeekTimeString(
    instant: Instant = Clock.System.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    val locale = LocalConfiguration.current.locales[0]
    val format = when (locale) {
        Locale.SIMPLIFIED_CHINESE -> TimeFormat.MONTH_DAY_WEEK_FORMAT_CHINESE
        else -> TimeFormat.MONTH_DAY_WEEK_FORMAT_ENGLISH
    }
    return instant.toLocalDateTime(timeZone).date.format(format)
}

@Composable
@ReadOnlyComposable
fun autoTimeString(
    instant: Instant = Clock.System.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    val format = TimeFormat.getAutoTimeFormat(
        isToday = instant.toLocalDateTime(timeZone).date == Clock.System.now()
            .toLocalDateTime(timeZone).date,
        is24HourFormat = is24HourFormat,
        locale = LocalConfiguration.current.locales[0]
    )
    return instant.toLocalDateTime(timeZone).format(format)
}
