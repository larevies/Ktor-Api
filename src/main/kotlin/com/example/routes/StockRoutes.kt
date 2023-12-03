package com.example.routes

import com.example.database.queries.StockQueries
import com.example.modules.Stock
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.stockRouting() {
    route("/user") {

        val stockQueries = StockQueries()


        /***
         * Получение всех акций опреденного портфеля определенного пользователя
         */
        get("/{id?}/portfolios/{portfolioid?}/stocks") {
            val id_portfolio = call.parameters["portfolioid"] ?: return@get call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            val stocksFromDB = stockQueries.getStockByPortfolio(id_portfolio.toInt())

            if (stocksFromDB != null && stocksFromDB.isNotEmpty()) {
                call.respond(stocksFromDB)
            } else {
                call.respondText(
                    "No stocks found for portfolio $id_portfolio",
                    status = HttpStatusCode.NotFound
                )
            }
        }

        /***
         * Вывод конкретной акции
         */
        get("/{id?}/portfolios/{portfolioid?}/stocks/{stockid?}") {
            val stockId = call.parameters["stockid"]?.toIntOrNull()
            if (stockId != null) {
                val stock = stockQueries.getStockByID(stockId)
                if (stock != null) {
                    call.respond(stock)
                } else {
                    call.respondText("No stock with id $stockId", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
            }
        }

        /***
         * Добавление акции в портфель пользователя
         */
        post("/{id?}/portfolios/{portfolioid?}/stocks") {
            val stock = call.receive<Stock>()

            stockQueries.addStock(stock.id_portfolio, stock.id_company,
                stock.amount, stock.name, stock.current_price, stock.purchase_price)

            call.respondText("Stock added correctly", status = HttpStatusCode.Created)
        }



        /***
         * Удаление акции из базы данных
         */
        delete("/{id?}/portfolios/{portfolioid?}/stocks/{stockid?}") {
            val stock_id = call.parameters["stockid"] ?: return@delete call.respondText(
                "Missing stock id",
                status = HttpStatusCode.BadRequest
            )


            if (stockQueries.getStockByID(stock_id.toInt()) != null) {
                stockQueries.deleteStock(stock_id.toInt())
                call.respondText("Stock removed", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}