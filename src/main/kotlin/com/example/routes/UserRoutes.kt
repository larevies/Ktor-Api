package com.example.routes

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

    val userQueries = UserQueries()

    route("/user") {

        /***
         * Получение всех пользователей
         */
        get {
            val usersFromDB = userQueries.getUsers()
            if (usersFromDB != null && usersFromDB.isNotEmpty()) {
                call.respond(usersFromDB)
            } else {
                call.respondText("No users found", status = HttpStatusCode.OK)
            }
        }


        /***
         * Получение пользователя по ID
         */

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val user = userQueries.getUserByID(id)
                if (user != null) {
                    call.respond(user)
                } else {
                    call.respondText("No user with id $id", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
            }
        }


        /***
         * Добавление нового пользователя
         */
        post {
            val customer = call.receive<User>()

            userQueries.addUser(customer.password, customer.name, customer.email)

            call.respondText("User added correctly", status = HttpStatusCode.Created)
        }


        /***
         * Удаление пользователя по ID
         */
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (userQueries.getUserByID(id.toInt()) != null) {
                userQueries.deleteUser(id.toInt())
                call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

    route("/login") {
        post {
            val intel = call.receive<Intel>()
            if (userQueries.authorization(intel.password, intel.password)) {
                return@post call.respond(HttpStatusCode.Accepted, true)
            } else {
                return@post call.respond(HttpStatusCode.Conflict)
            }
        }
    }
}
