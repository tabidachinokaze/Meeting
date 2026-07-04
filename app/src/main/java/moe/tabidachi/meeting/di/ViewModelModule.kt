package moe.tabidachi.meeting.di

import moe.tabidachi.meeting.ui.auth.AuthViewModel
import moe.tabidachi.meeting.ui.main.MainViewModel
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingViewModel
import moe.tabidachi.meeting.ui.meeting.created.MeetingCreatedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            context = get(),
            authApi = get(),
            dataStore = get(),
        )
    }
    viewModel {
        MainViewModel(
            context = get(),
            userApi = get(),
            dataStore = get(),
            meetingApi = get()
        )
    }
    viewModel {
        CreateMeetingViewModel(
            context = get(),
            userApi = get(),
            meetingApi = get(),
        )
    }
    viewModel {
        MeetingCreatedViewModel(
            route = it.get(),
        )
    }
}