package com.example.routes
import com.example.database.queries.CompanyQueries
import com.example.modules.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.companyRouting() {

    val companyQueries = CompanyQueries()

    route("/company") {
        get {

            companyQueries.getCompanies()

            if (companies.isNotEmpty()) {
                call.respond(companies)
            } else {
                call.respondText("No companies found", status = HttpStatusCode.OK)
            }
        }

        post {
            val company = call.receive<Company>()

            companyQueries.addCompany(company.name,company.current_price)

            companies.add(company)
            call.respondText("Company added correctly", status = HttpStatusCode.Created)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            companyQueries.deleteCompany(id.toInt())

            val removedCompany = companies.find { it.id == id }
            if (removedCompany != null) {
                stocks.removeAll { it.id_company == id }
                companies.remove(removedCompany)
                call.respondText("Company removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}