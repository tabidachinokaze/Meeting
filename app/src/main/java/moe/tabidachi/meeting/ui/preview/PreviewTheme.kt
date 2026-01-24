package moe.tabidachi.meeting.ui.preview

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.theme.MeetingTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PreviewTheme(
    isAsyncImagePreviewEnabled: Boolean = false,
    content: @Composable () -> Unit
) = MeetingTheme {
    if (isAsyncImagePreviewEnabled) {
        val context = LocalContext.current
        CompositionLocalProvider(
            LocalAsyncImagePreviewHandler provides AsyncImagePreviewHandler {
                AppCompatResources.getDrawable(context, R.drawable.ic_launcher_background)!!
                    .asImage()
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = content
            )
        }
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}