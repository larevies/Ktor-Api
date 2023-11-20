package com.example.database.queries

import com.example.database.connection.*
import com.example.modules.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class CompanyQueries {
    private var connection: Connection? = null

    init {
        try {
            connection = DriverManager.getConnection(url, username, DBpassword)

        } catch (e: SQLException) {
            println(errorMessage)
            println("${e.message}")
        }
    }

    fun addCompany(name : String, currentPrice : Double) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(
                """INSERT INTO public."Company"(name, current_price)
	                    VALUES ('${name}', ${currentPrice}); 
                    """
            )
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }

    fun getCompanies(): List<Company>? {
        val companies = mutableListOf<Company>()
        return try{
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, name, current_price FROM public."Company"""")
            resultSet?.let {
                while (resultSet.next()) {
                    val company = Company(
                        id = resultSet.getString("id_company"),
                        name = resultSet.getString("name"),
                        current_price = resultSet.getDouble("current_price")
                    )
                    companies.add(company)
                }
                resultSet.close()
            }
            companies
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
            null
        }
    }

    fun getCompanyByID(id : Int): List<Company>? {
        val companies = mutableListOf<Company>()
        return try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, name, current_price 
                |                                      FROM "Company" 
                |                                      WHERE id = ${id} 
            """.trimMargin())
            resultSet?.let {
                while (resultSet.next()) {
                    val company = Company(
                        id = resultSet.getString("id"),
                        name = resultSet.getString("name"),
                        current_price = resultSet.getDouble("email")
                    )
                    companies.add(company)
                }
                resultSet.close()
            }
            companies
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
            null
        }
    }
}