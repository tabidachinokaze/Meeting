package moe.tabidachi.meeting.ui.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tabidachi.meeting.ui.theme.MeetingTheme

@Composable
fun PreviewTheme(content: @Composable () -> Unit) = MeetingTheme {
    Surface(
        modifier = Modifier.fillMaxSize(),
        content = content
    )
}