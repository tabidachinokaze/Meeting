package moe.tabidachi.meeting

import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.todayIn
import moe.tabidachi.meeting.utils.TimeFormat
import org.junit.Test
import kotlin.time.Clock

class LocalDateTest {
    @Test
    fun testLocaleDateFormat() {
        val localDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        println(localDate.format(TimeFormat.MONTH_DAY_WEEK_FORMAT_CHINESE))
        println(localDate.format(TimeFormat.MONTH_DAY_WEEK_FORMAT_ENGLISH))
    }

    @Test
    fun testLocalTimeFormat() {
        val instant = Clock.System.now()
        val format24H = instant.format(
            format = DateTimeComponents.Format {
                this.time(TimeFormat.HOUR_MINUTE_24H_FORMAT)
            }
        )
        val format12HEnglish = instant.format(
            format = DateTimeComponents.Format {
                this.time(TimeFormat.HOUR_MINUTE_12H_FORMAT_ENGLISH)
            }
        )
        val format12HChinese = instant.format(
            format = DateTimeComponents.Format {
                this.time(TimeFormat.HOUR_MINUTE_12H_FORMAT_CHINESE)
            }
        )
        println(format12HEnglish)
        println(format12HChinese)
        println(format24H)
    }
}