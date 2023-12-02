package com.example.plugins


import com.example.routes.companyRouting
import com.example.routes.portfolioRouting
import com.example.routes.stockRouting

import com.example.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


/***
 * Конфигурация маршрутизации
 */
fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("our super cool and unfinished http api!")
        }
        userRouting()
        portfolioRouting()
        stockRouting()
        companyRouting()

    }
}
