package com.example.routes

import com.example.database.queries.PortfolioQueries
import com.example.modules.Portfolio
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.portfolioRouting() {

    val portfolioQueries = PortfolioQueries()


    route("/portfolio") {
        /***
         * Получение всех портфелей пользователя по его ID
         */
        get("user/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val portfoliosFromDB = portfolioQueries.getPortfolioByUser(id.toInt())

            if (portfoliosFromDB != null && portfoliosFromDB.isNotEmpty()) {
                call.respond(portfoliosFromDB)
            } else {
                call.respondText("No portfolios were found for user with id ${id}", status = HttpStatusCode.OK)
            }
        }

        /***
         * Вывод конкретного портфеля
         */
        get("{id?}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val portfolio = portfolioQueries.getPortfolioByID(id)
                if (portfolio != null) {
                    call.respond(portfolio)
                } else {
                    call.respondText("No portfolio with id $id", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
            }
        }

        /***
         * Вывод вообще всех портфелей (вдруг надо)
         */
        get {
            val portfolios = portfolioQueries.getPortfolios()
            if (portfolios != null) {
                call.respond(portfolios)
            } else {
                call.respondText("No portfolio found", status = HttpStatusCode.NotFound)
            }
        }

        /***
         * Добавление портфеля пользователю
         */
        post {
            val portfolio = call.receive<Portfolio>()

            portfolioQueries.addPortfolio(portfolio.user_id, portfolio.name,
                                        portfolio.price, portfolio.total_profit,
                                        portfolio.profitability, portfolio.change_day)

            call.respondText("Portfolio added correctly", status = HttpStatusCode.Created)
        }

        /***
         * Удаление портфеля по ID
         */
        delete("{id?}") {

            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            if (portfolioQueries.getPortfolioByID(id.toInt()) != null) {
                portfolioQueries.deletePortfolio(id.toInt())
                call.respondText("Portfolio removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}