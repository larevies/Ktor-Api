package com.example.routes


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.modules.*
import com.example.modules.Currency


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
import java.util.*

fun Route.userRouting() {

    var id_user: String ? = null

    route("/user") {


        /**
         * GET делает запрос на http сайтик и получает для нас какой-то ответ
         *
         * Пример работы GET:
         * GET http://www.example.com/users  -  получаем всех юзеров
         * GET http://www.example.com/users/12345  -  получаем юзера 12345
         * ИЛИ
         * GET http://www.example.com/customers?name=лера  -  то же самое что до этого
         *
         * Выше
         * Первый GET получает список всех пользователей
         * Второй GET получает конкретного пользователя
         *
         * Пример работы GET:
         * curl -X GET --location "http://127.0.0.1:8080/user" -H "Accept: application/json"
         */


        get {
            if (users.isNotEmpty()) {
                call.respond(users)
            } else {
                call.respondText("No users found", status = HttpStatusCode.OK)
            }
        }


        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                users.find { it.id == id } ?: return@get call.respondText(
                    "No user with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }


        /**
         * POST добавляет на сайт что-то новое.
         * Этот POST добавляет одного нового пользователя
         *
         * Пример работы POST:
         * curl -X POST --location "http://127.0.0.1:8080/user"  \
         *     -H "Content-Type: application/json"  \
         *     -d "{
         *           \"id\": \"338828\",
         *           \"firstName\": \"Лера\",
         *           \"lastName\": \"Серебренникова\",
         *           \"email\": \"лерин_имейл@майл.ру\"
         *         }"
         */


        post {
            val customer = call.receive<User>()
            users.add(customer)
            call.respondText("User added correctly", status = HttpStatusCode.Created)
        }


        /**
         * DELETE удаляет что-то с сайта.
         * Этот DELETE удаляет пользователя из списка "пользователи"
         *
         * Пример работы DELETE:
         * curl -X DELETE --location "http://127.0.0.1:8080/customer/11111"
         */


        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            val removedUser = users.find{ it.id == id }
            if (removedUser != null){
                users.remove(removedUser)

                val removedPortfolios = portfolios.filter { it.user_id == id }
                portfolios.removeAll { it.user_id == id }

                val removedStocks = stocks.filter { it.id_portfolio in removedPortfolios.map{ it.id }}
                stocks.removeAll { it.id_portfolio in removedPortfolios.map{ it.id }}
                call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

        post("/login") {
            val user = call.receive<User>()
            val found = users.find {it.email == user.email && it.password == user.password}
            if (found != null) {
                id_user = user.id
            }
            println(id_user)


            // Check username and password
            // ...
            /*val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("name", user.name)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))
        call.respond(hashMapOf("token" to token))*/
        }

        post("/{id?}/portfolios") {
            val portfolio = call.receive<Portfolio>()
            portfolios.add(portfolio)
            call.respondText("Portfolio added correctly", status = HttpStatusCode.Created)
        }

        get ("/{id?}/portfolios") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val matchingPortfolios = portfolios.filter { it.user_id == id }
            if (matchingPortfolios.isNotEmpty()) {
                call.respond(matchingPortfolios)
            } else {
                return@get call.respondText (
                    "No portfolios for user with id $id",
                    status = HttpStatusCode.NotFound)
            }
        }

        delete ("/{id?}/portfolios/{portfolioid?}") {
            val user_id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing user id",
                status = HttpStatusCode.BadRequest
            )
            val id_portfolio = call.parameters["portfolioid"] ?: return@delete call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            val matchingPortfolios = portfolios.filter { it.user_id == user_id }
            val removedPortfolios = matchingPortfolios.find { it.id == id_portfolio }
            if (removedPortfolios != null &&
                portfolios.removeIf{it.id == id_portfolio} &&
                stocks.removeIf{it.id_portfolio == id_portfolio})
            {
                call.respondText("Portfolio removed correctly", status = HttpStatusCode.Accepted)

            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

        post("/{id?}/portfolios/{portfolioid?}/stocks") {
            val stock = call.receive<Stock>()
            stocks.add(stock)
            call.respondText("Stock added correctly", status = HttpStatusCode.Created)
        }

        get ("/{id?}/portfolios/{portfolioid?}/stocks") {
            val id_user_get_stocks = call.parameters["id"] ?: return@get call.respondText(
                "Missing user id",
                status = HttpStatusCode.BadRequest
            )

            val id_portfolio = call.parameters["portfolioid"] ?: return@get call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            val matchingPortfolio = portfolios.find { it.id == id_portfolio && it.user_id == id_user_get_stocks }
            if (matchingPortfolio != null) {
                val matchingStocks = stocks.filter { it.id_portfolio == matchingPortfolio.id }
                call.respond(matchingStocks)
            } else {
                return@get call.respondText (
                    "No portfolios for user with id $id_user_get_stocks",
                    status = HttpStatusCode.NotFound)
            }
        }

        delete ("/{id?}/portfolios/{portfolioid?}/stocks/{stockid?}") {
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

            val matchingPortfolio = portfolios.find { it.id == id_portfolio && it.user_id == user_id }
            if (matchingPortfolio != null) {
                val matchingStock = stocks.find { it.id_portfolio == matchingPortfolio.id && it.id == stock_id}
                if (matchingStock != null){
                    stocks.remove(matchingStock)
                    call.respondText("Stock removed from portfolio", status = HttpStatusCode.Accepted)
                }else{
                    call.respondText("Stock not found in the specified portfolio", status = HttpStatusCode.NotFound)
                }

            } else {
                call.respondText("No portfolio found for the specified user and portfolio id", status = HttpStatusCode.NotFound)
            }
        }


        //Удаление акций при удалении портфеля
        delete("/{id?}/portfolios/{portfolioid?}") {
            val user_id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing user id",
                status = HttpStatusCode.BadRequest
            )
            val id_portfolio = call.parameters["portfolioid"] ?: return@delete call.respondText(
                "Missing portfolio id",
                status = HttpStatusCode.BadRequest
            )

            val matchingPortfolio = portfolios.find { it.id == id_portfolio && it.user_id == user_id }
            if (matchingPortfolio != null) {
                stocks.removeAll { it.id_portfolio == matchingPortfolio.id }

                if (portfolios.remove(matchingPortfolio)) {
                    call.respondText("Portfolio removed correctly, along with associated stocks", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
//
//        //Удаление портфелей и акций при удалении пользователя
//        delete("{id?}") {
//            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
//            val removedUser = users.find { it.id == id }
//
//            if (removedUser != null) {
//                portfolios.removeAll { it.user_id == id }
//                stocks.removeAll { it.user_id == id }
//
//                if (users.remove(removedUser)) {
//                    call.respondText("User removed correctly, along with associated portfolios and stocks", status = HttpStatusCode.Accepted)
//                } else {
//                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
//                }
//            } else {
//                call.respondText("Not Found", status = HttpStatusCode.NotFound)
//            }
//        }

    }


}