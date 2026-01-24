package moe.tabidachi.meeting.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import java.util.Locale

@Composable
@ReadOnlyComposable
fun getLocale(): Locale {
    if (LocalInspectionMode.current) return Locale.CHINA
    val configuration = LocalConfiguration.current
    return configuration.locales[0]
}