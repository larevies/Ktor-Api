package com.example.routes

import com.example.modules.Currency
import com.example.modules.currencies
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.currencyRouting() {
    route("/currency") {
        get {
            if (currencies.isNotEmpty()) {
                call.respond(currencies)
            } else {
                call.respondText("No currencies found", status = HttpStatusCode.OK)
            }
        }
        post {
            val currency = call.receive<Currency>()
            currencies.add(currency)
            call.respondText("Currency added correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (currencies.removeIf { it.id == id }) {
                call.respondText("Currency removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}