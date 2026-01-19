package moe.tabidachi.meeting.routing

import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import moe.tabidachi.meeting.model.LoginRequest
import moe.tabidachi.meeting.model.SignupRequest
import moe.tabidachi.meeting.service.AuthService

fun Route.authenticate() {
    val authService: AuthService by application.dependencies
    post<SignupRequest>("/signup") { request ->
        call.respond(authService.signup(request))
    }
    post<LoginRequest>("/login") { request ->
        call.respond(authService.login(request))
    }
}
