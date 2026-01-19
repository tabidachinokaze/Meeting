package moe.tabidachi.meeting.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import moe.tabidachi.meeting.routing.authenticate

fun Application.configureRouting() {
    install(SSE)
    routing {
        sse("/sse") {

        }
    }
    routing {
        authenticate()
    }
}