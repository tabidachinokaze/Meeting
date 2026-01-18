package moe.tabidachi.meeting.di

import moe.tabidachi.meeting.ui.auth.auth
import org.koin.dsl.module

val routeModule = module {
    auth()
}