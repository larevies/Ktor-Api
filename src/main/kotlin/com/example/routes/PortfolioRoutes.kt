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

    route("/user") {

        /***
         * Получение всех портфелей пользователя по его ID
         */
        get("/{id?}/portfolios") {
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
        get("/{id?}/portfolios/{portfolioid?}") {
            val portfolioId = call.parameters["portfolioid"]?.toIntOrNull()
            if (portfolioId != null) {
                val portfolio = portfolioQueries.getPortfolioByID(portfolioId)
                if (portfolio != null) {
                    call.respond(portfolio)
                } else {
                    call.respondText("No portfolio with id $portfolioId", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
            }
        }

        /***
         * Добавление портфеля пользователю
         */
        post("/{id?}/portfolios") {
            val portfolio = call.receive<Portfolio>()

            portfolioQueries.addPortfolio(portfolio.user_id, portfolio.name,
                                        portfolio.price, portfolio.total_profit,
                                        portfolio.profitability, portfolio.change_day)

            call.respondText("Portfolio added correctly", status = HttpStatusCode.Created)
        }

        /***
         * Удаление портфеля по ID
         */
        delete("/{id?}/portfolios/{portfolioid?}") {

            val portfolio_id = call.parameters["portfolioid"] ?: return@delete call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            if (portfolioQueries.getPortfolioByID(portfolio_id.toInt()) != null) {
                portfolioQueries.deletePortfolio(portfolio_id.toInt())
                call.respondText("Portfolio removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}