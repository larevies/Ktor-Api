package com.example.routes
import com.example.database.queries.CompanyQueries
import com.example.modules.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


/***
 * Маршруты для компаний
 */
fun Route.companyRouting() {

    val companyQueries = CompanyQueries()

    route("/company") {

        /***
         * Получение всех компаний
         */
        get {
            val companiesFromDB = companyQueries.getCompanies()
            if (companiesFromDB != null && companiesFromDB.isNotEmpty()) {
                call.respond(companiesFromDB)
            } else {
                call.respondText("No companies found", status = HttpStatusCode.OK)
            }
        }

        /***
         * Получение конкретной компании по ID
         */

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val company = companyQueries.getCompanyByID(id)
                if (company != null) {
                    call.respond(company)
                } else {
                    call.respondText("No company with id $id", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
            }
        }

        /***
         * Добавление компании
         */
        post {
            val company = call.receive<Company>()

            companyQueries.addCompany(company.name,company.current_price)

            //companies.add(company)
            call.respondText("Company added correctly", status = HttpStatusCode.Created)
        }

        /***
         * Удаление компании по ID
         */
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (companyQueries.getCompanyByID(id.toInt()) != null) {
                companyQueries.deleteCompany(id.toInt())
                call.respondText("Company removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}