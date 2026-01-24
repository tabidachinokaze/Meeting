package moe.tabidachi.meeting.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.main.page.HomePage
import moe.tabidachi.meeting.ui.main.page.MeetingsPage
import moe.tabidachi.meeting.ui.main.page.SettingsPage
import moe.tabidachi.meeting.ui.main.page.TasksPage
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews

@Composable
fun MainScreen(
    state: MainContract.State,
    actions: MainContract.Actions
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach {
                    NavigationBarItem(
                        selected = state.currentTab == it,
                        onClick = {
                            actions.onTabClick(it)
                        },
                        icon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.icon.name
                            )
                        },
                        label = {
                            Text(text = stringResource(it.label))
                        },
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(bottom = it.calculateBottomPadding()))
        ) {
            when (state.currentTab) {
                MainTab.Home -> HomePage()
                MainTab.Meetings -> MeetingsPage()
                MainTab.Tasks -> TasksPage()
                MainTab.Settings -> SettingsPage(
                    state = state,
                    actions = actions,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

enum class MainTab(
    val icon: ImageVector,
    @StringRes
    val label: Int
) {
    Home(icon = Icons.Outlined.Home, label = R.string.main_screen_tab_home),
    Meetings(icon = Icons.Outlined.CalendarToday, label = R.string.main_screen_tab_meetings),
    Tasks(icon = Icons.Outlined.Checklist, label = R.string.main_screen_tab_tasks),
    Settings(icon = Icons.Outlined.Person, label = R.string.main_screen_tab_settings)
}

@Composable
@Previews
private fun MainScreenPreview() = PreviewTheme {
    MainScreen(
        state = MainContract.State.Preview,
        actions = MainContract.Actions()
    )
}