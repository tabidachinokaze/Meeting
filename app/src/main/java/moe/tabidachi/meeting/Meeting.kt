package moe.tabidachi.meeting

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.gif.AnimatedImageDecoder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder
import coil3.video.VideoFrameDecoder
import io.ktor.client.HttpClient
import moe.tabidachi.meeting.di.appModule
import moe.tabidachi.meeting.di.routeModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class Meeting : Application(), KoinStartup, SingletonImageLoader.Factory {
    private val client: HttpClient by inject()

    override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
        androidLogger(level = Level.DEBUG)
        androidContext(this@Meeting)
        modules(routeModule, appModule)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(KtorNetworkFetcherFactory(client))
                add(AnimatedImageDecoder.Factory())
                add(SvgDecoder.Factory())
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }
}
