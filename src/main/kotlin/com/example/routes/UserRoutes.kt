package com.example.routes


import com.example.database.connection.ConnectionDB
import com.example.database.queries.PortfolioQueries
import com.example.database.queries.StockQueries
import com.example.database.queries.UserQueries
import com.example.modules.*


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

import io.ktor.server.routing.Route

import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.route


/***
 * Маршруты для пользователей
 */
fun Route.userRouting() {
    val connectionDB = ConnectionDB()
    val userQueries = UserQueries()
    val portfolioQueries = PortfolioQueries()
    val stockQueries = StockQueries()

    route("/user") {

        /***
         * Получение всех пользователей
         */
        get {
            if (users.isNotEmpty()) {
                call.respond(users)
                userQueries.getUsers()
            } else {
                call.respondText("No users found", status = HttpStatusCode.OK)
            }
        }


        /***
         * Получение пользователя по ID
         */

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val user = userQueries.getUserByID(id.toInt())

            if (user != null) {
                call.respond(user)
            } else {
                call.respondText(
                    "No user with id $id",
                    status = HttpStatusCode.NotFound
                )
            }
        }

//        get("{id?}") {
//            val id = call.parameters["id"] ?: return@get call.respondText(
//                "Missing id",
//                status = HttpStatusCode.BadRequest
//            )
//
//            userQueries.getUserByID(id.toInt())
//
//            val customer =
//                users.find { it.id == id } ?: return@get call.respondText(
//                    "No user with id $id",
//                    status = HttpStatusCode.NotFound
//                )
//            call.respond(customer)
//        }


        /***
         * Добавление нового пользователя
         */
        post {
            val customer = call.receive<User>()

            userQueries.addUser(customer.password, customer.name, customer.email)

            users.add(customer)
            call.respondText("User added correctly", status = HttpStatusCode.Created)
        }


        /***
         * Удаление пользователя по ID
         */
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            userQueries.deleteUser(id.toInt())

            val removedUser = users.find { it.id == id }
            if (removedUser != null) {
                users.remove(removedUser)

                val removedPortfolios = portfolios.filter { it.user_id == id }
                portfolios.removeAll { it.user_id == id }

                val removedStocks = stocks.filter { it.id_portfolio in removedPortfolios.map { it.id } }
                //все стокс

                for (stock in removedStocks) {
                    stocks.removeAll { it.id_portfolio == stock.id }
                }
                call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

        /***
         * Добавление портфеля пользователю
         */
        post("/{id?}/portfolios") {
            val portfolio = call.receive<Portfolio>()
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            portfolioQueries.addPortfolio(portfolio.name, id)

            portfolios.add(portfolio)
            call.respondText("Portfolio added correctly", status = HttpStatusCode.Created)
        }

        /***
         * Получение всех портфелей пользователя по его ID
         */
        get("/{id?}/portfolios") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            portfolioQueries.getPortfolioByID(id.toInt())

            val matchingPortfolios = portfolios.filter { it.user_id == id }
            if (matchingPortfolios.isNotEmpty()) {
                call.respond(matchingPortfolios)
            } else {
                return@get call.respondText(
                    "No portfolios for user with id $id",
                    status = HttpStatusCode.NotFound
                )
            }
        }

        /***
         * Удаление портфеля по ID
         */
        delete("/{id?}/portfolios/{portfolioid?}") {
            val user_id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing user id",
                status = HttpStatusCode.BadRequest
            )
            val id_portfolio = call.parameters["portfolioid"] ?: return@delete call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            portfolioQueries.deletePortfolio(id_portfolio.toInt())

            val matchingPortfolios = portfolios.filter { it.user_id == user_id }
            val removedPortfolios = matchingPortfolios.find { it.id == id_portfolio }
            if (removedPortfolios != null &&
                portfolios.removeIf { it.id == id_portfolio } &&
                stocks.removeIf { it.id_portfolio == id_portfolio }
            ) {
                call.respondText("Portfolio removed correctly", status = HttpStatusCode.Accepted)

            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }


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
