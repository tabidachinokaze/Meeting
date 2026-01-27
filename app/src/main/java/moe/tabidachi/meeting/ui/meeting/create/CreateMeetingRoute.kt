package moe.tabidachi.meeting.ui.meeting.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.navigation3.navigation

@Serializable
data object CreateMeetingRoute : NavKey

@Composable
fun CreateMeetingRoute(
    viewModel: CreateMeetingViewModel
) {
    val (state, event) = viewModel.observe { }
    CreateMeetingScreen(
        state = state.value,
        actions = remember {
            CreateMeetingContract.Actions()
        }
    )
}

@OptIn(KoinExperimentalAPI::class)
fun Module.createMeeting() {
    viewModel {
        CreateMeetingViewModel()
    }
    navigation<CreateMeetingRoute> {
        CreateMeetingRoute(
            viewModel = get()
        )
    }
}
