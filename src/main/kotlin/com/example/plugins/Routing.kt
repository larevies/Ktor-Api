package com.example.plugins


import com.example.routes.companyRouting
import com.example.routes.currencyRouting

import com.example.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("our super cool and unfinished http api!")
        }
        userRouting()
        currencyRouting()
        companyRouting()

    }
}