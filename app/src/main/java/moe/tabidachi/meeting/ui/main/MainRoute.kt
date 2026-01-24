package moe.tabidachi.meeting.ui.main

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
data object MainRoute : NavKey

@Composable
fun MainRoute(
    viewModel: MainViewModel
) {
    val (state, event) = viewModel.observe { }
    MainScreen(
        state = state.value,
        actions = remember {
            MainContract.Actions(
                onTabClick = { event(MainContract.Event.OnTabClick(it)) }
            )
        }
    )
}

@OptIn(KoinExperimentalAPI::class)
fun Module.main() {
    viewModel {
        MainViewModel()
    }
    navigation<MainRoute> {
        MainRoute(
            viewModel = get()
        )
    }
}