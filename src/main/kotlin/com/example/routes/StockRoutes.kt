package com.example.routes

import com.example.database.queries.StockQueries
import com.example.modules.Stock
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.stockRouting() {

    route("/stock") {

        val stockQueries = StockQueries()


        /***
         * Получение всех акций опреденного портфеля определенного пользователя
         */
        get("portfolio/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            val stocksFromDB = stockQueries.getStockByPortfolio(id.toInt())

            if (stocksFromDB != null && stocksFromDB.isNotEmpty()) {
                call.respond(stocksFromDB)
            } else {
                call.respondText(
                    "No stocks found for portfolio $id",
                    status = HttpStatusCode.NotFound
                )
            }
        }

        /***
         * Вывод конкретной акции
         */
        get("{id?}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val stock = stockQueries.getStockByID(id)
                if (stock != null) {
                    call.respond(stock)
                } else {
                    call.respondText("No stock with id $id", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
            }
        }


        /***
         * Добавление акции в портфель пользователя
         */
        post {
            val stock = call.receive<Stock>()

            stockQueries.addStock(stock.idPortfolio, stock.idCompany,
                stock.amount, stock.name, stock.currentPrice, stock.purchasePrice)

            call.respondText("Stock added correctly", status = HttpStatusCode.Created)
        }



        /***
         * Удаление акции из базы данных
         */
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing stock id",
                status = HttpStatusCode.BadRequest
            )


            if (stockQueries.getStockByID(id.toInt()) != null) {
                stockQueries.deleteStock(id.toInt())
                call.respondText("Stock removed", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}