package com.example.database.queries

import com.example.database.connection.*
import com.example.modules.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


/***
 * Следующий класс содержит функции, выполняющие SQL запросы в базу данных к таблице "Компании".
 */
class CompanyQueries {

    private var connection: Connection? = null

    init {
        try {
            connection = DriverManager.getConnection(url, username, DBPassword)

        } catch (e: SQLException) {
            println(errorMessage)
            println("${e.message}")
        }
    }


    /***
     * Добавление компании в базу данных
     */
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

    /***
     * Получение всех компаний из базы данных
     */
    fun getCompanies(): List<Company>? {
        val companies = mutableListOf<Company>()
        return try{
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, name, current_price FROM public."Company"""")
            resultSet?.let {
                while (resultSet.next()) {
                    val company = Company(
                        id = resultSet.getString("id"),
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

    /***
     * Получение компании по ID
     */
    fun getCompanyByID(id : Int): Company? {
        var company: Company? = null

        try {
            val statement = connection?.prepareStatement(
                """SELECT id, name, current_price FROM public."Company" WHERE id = ? """
            )

            statement?.setInt(1, id ?: 0)

            val resultSet = statement?.executeQuery()

            resultSet?.use {
                if (resultSet.next()) {
                    company = Company(
                        id = resultSet.getString("id"),
                        name = resultSet.getString("name"),
                        current_price = resultSet.getDouble("current_price")
                    )
                }
            }
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
        return company
    }

    /***
     * Удаление компании из базы данных по ID
     */
    fun deleteCompany(id : Int) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(""" DELETE FROM "Company" WHERE id = ${id} """)
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }
}