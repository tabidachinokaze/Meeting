package moe.tabidachi.meeting.ui

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.navigation3.EntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MeetingNavDisplay(
    backStack: NavBackStack<NavKey>,
    entryProvider: EntryProvider<NavKey>,
) {
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider
    )
}