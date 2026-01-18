package moe.tabidachi.meeting

import android.app.Application
import moe.tabidachi.meeting.di.routeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class Meeting : Application(), KoinStartup {
    override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
        androidLogger(level = Level.DEBUG)
        androidContext(this@Meeting)
        modules(routeModule)
    }
}
