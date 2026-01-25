package moe.tabidachi.meeting.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import moe.tabidachi.meeting.config.JwtConfig
import moe.tabidachi.meeting.routing.authenticate
import moe.tabidachi.meeting.routing.user

fun Application.configureRouting() {
    install(SSE)
    routing {
        sse("/sse") {

        }
    }
    val jwtConfig = property<JwtConfig>("jwt")
    routing {
        authenticate()
        authenticate(jwtConfig.name) {
            user()
        }
    }
}