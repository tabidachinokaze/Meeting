package moe.tabidachi.meeting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import moe.tabidachi.meeting.ui.MeetingNavDisplay
import moe.tabidachi.meeting.ui.theme.MeetingTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.navigation3.entryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity() {
    private val entryProvider by entryProvider<NavKey>()
    private val startDestination: NavKey by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeetingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MeetingNavDisplay(
                        entryProvider = entryProvider,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
