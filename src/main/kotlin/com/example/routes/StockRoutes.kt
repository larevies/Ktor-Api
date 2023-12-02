package com.example.routes

import com.example.database.queries.StockQueries
import com.example.modules.Stock
import com.example.modules.portfolios
import com.example.modules.stocks
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.stockRouting() {
    route("/user") {

        val stockQueries = StockQueries()

        /***
         * Добавление акции в портфель пользователя
         */
        post("/{id?}/portfolios/{portfolioid?}/stocks") {
            val stock = call.receive<Stock>()

            stockQueries.addStock(stock.id_portfolio, stock.id_portfolio,
                stock.amount, stock.name, stock.current_price, stock.purchase_price)

            stocks.add(stock)
            call.respondText("Stock added correctly", status = HttpStatusCode.Created)
        }


        /***
         * Получение всех акций опреденного портфеля определенного пользователя
         */
        get("/{id?}/portfolios/{portfolioid?}/stocks") {
            val id_user_get_stocks = call.parameters["id"] ?: return@get call.respondText(
                "Missing user id",
                status = HttpStatusCode.BadRequest
            )

            val id_portfolio = call.parameters["portfolioid"] ?: return@get call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            stockQueries.getStockByPortfolio(id_portfolio.toInt())

            val matchingPortfolio = portfolios.find { it.id == id_portfolio && it.user_id == id_user_get_stocks }


            if (matchingPortfolio != null) {
                val matchingStocks = stocks.filter { it.id_portfolio == matchingPortfolio.id }
                call.respond(matchingStocks)
            } else {
                return@get call.respondText(
                    "No portfolios for user with id $id_user_get_stocks",
                    status = HttpStatusCode.NotFound
                )
            }
        }

        /***
         * Удаление акции из базы данных
         */
        delete("/{id?}/portfolios/{portfolioid?}/stocks/{stockid?}") {
            val user_id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing user id",
                status = HttpStatusCode.BadRequest
            )
            val id_portfolio = call.parameters["portfolioid"] ?: return@delete call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            val stock_id = call.parameters["stockid"] ?: return@delete call.respondText(
                "Missing stock id",
                status = HttpStatusCode.BadRequest
            )

            stockQueries.getStockByID(stock_id.toInt())

            val matchingPortfolio = portfolios.find { it.id == id_portfolio && it.user_id == user_id }
            if (matchingPortfolio != null) {
                val matchingStock = stocks.find { it.id_portfolio == matchingPortfolio.id && it.id == stock_id }
                if (matchingStock != null) {
                    stocks.remove(matchingStock)
                    call.respondText("Stock removed from portfolio", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Stock not found in the specified portfolio", status = HttpStatusCode.NotFound)
                }

            } else {
                call.respondText(
                    "No portfolio found for the specified user and portfolio id",
                    status = HttpStatusCode.NotFound
                )
            }
        }
    }
}