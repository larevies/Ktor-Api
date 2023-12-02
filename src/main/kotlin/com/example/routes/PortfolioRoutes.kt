package com.example.routes

import com.example.database.queries.PortfolioQueries
import com.example.modules.Portfolio
import com.example.modules.portfolios
import com.example.modules.stocks
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
         * Добавление портфеля пользователю
         */
        post("/{id?}/portfolios") {
            val portfolio = call.receive<Portfolio>()
            //val id = call.parameters["id"]?.toIntOrNull()

            portfolioQueries.addPortfolio(portfolio.user_id, portfolio.name,
                                        portfolio.price, portfolio.total_profit,
                                        portfolio.profitability, portfolio.change_day)


            portfolios.add(portfolio)
            call.respondText("Portfolio added correctly", status = HttpStatusCode.Created)
        }

        /***
         * Удаление портфеля по ID
         */
        delete("/{id?}/portfolios/{portfolioid?}") {
            //val user_id = call.parameters["id"] ?: return@delete call.respondText(
            //    "Missing user id",
            //    status = HttpStatusCode.BadRequest
            //)
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



            /*val matchingPortfolios = portfolios.filter { it.user_id == user_id }
            val removedPortfolios = matchingPortfolios.find { it.id == portfolio_id }
            if (removedPortfolios != null &&
                portfolios.removeIf { it.id == portfolio_id } &&
                stocks.removeIf { it.id_portfolio == portfolio_id }
            ) {
                call.respondText("Portfolio removed correctly", status = HttpStatusCode.Accepted)

            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }*/
        }
    }
}