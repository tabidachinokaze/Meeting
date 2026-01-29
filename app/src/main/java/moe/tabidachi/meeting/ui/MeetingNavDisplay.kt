package moe.tabidachi.meeting.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.navigation3.EntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MeetingNavDisplay(
    backStack: NavBackStack<NavKey>,
    entryProvider: EntryProvider<NavKey>,
) {
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }
    NavDisplay(
        backStack = backStack,
        sceneStrategy = dialogStrategy,
        entryProvider = entryProvider
    )
}