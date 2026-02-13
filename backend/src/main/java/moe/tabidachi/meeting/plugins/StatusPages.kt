package moe.tabidachi.meeting.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import moe.tabidachi.meeting.model.StatusCode
import moe.tabidachi.meeting.model.emptyData
import moe.tabidachi.meeting.model.withData

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respond(StatusCode.InternalError.emptyData())
        }
    }
}