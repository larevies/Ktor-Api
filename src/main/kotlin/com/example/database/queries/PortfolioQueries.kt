package com.example.database.queries

import com.example.database.connection.*
import com.example.modules.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


/***
 * Следующий класс содержит функции, выполняющие SQL запросы в базу данных к таблице "Портфели".
 */
class PortfolioQueries {
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
     * Добавление портфеля в базу данных
     */
    fun addPortfolio(pName : String, idUser : String) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(
                """INSERT INTO public."Portfolio"(name, id_user)
	                    VALUES ('${pName}', '${idUser}');"""
            )
        } catch (e: SQLException) {
           // println(queryError)
          //  println("${e.message}")
        }
    }

    /***
     * Получение всех портфелей из базы данных
     */
    fun getPortfolios(): List<Portfolio>? {
        val portfolios = mutableListOf<Portfolio>()
        return try{
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, id_user, name, price, total_profit,
                | profitability, change_day FROM public."Portfolio"""".trimMargin())
            resultSet?.let {
                while (resultSet.next()) {
                    val portfolio = Portfolio(
                        id = resultSet.getString("id"),
                        user_id = resultSet.getString("id_user"),
                        name = resultSet.getString("name"),
                        price = resultSet.getDouble("price"),
                        total_profit = resultSet.getDouble("total_profit"),
                        profitability = resultSet.getDouble("profitability"),
                        change_day = resultSet.getDouble("change_day")
                    )
                    portfolios.add(portfolio)
                }
                resultSet.close()
            }
            portfolios
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
            null
        }
    }

    /***
     * Получение портфеля из базы данных по ID
     */
    fun getPortfolioByID(id : Int): List<Portfolio>? {
        val portfolios = mutableListOf<Portfolio>()
        return try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, id_user, name, price, total_profit,
                | profitability, change_day  FROM public."Portfolio" WHERE id = ${id} """)
            resultSet?.let {
                while (resultSet.next()) {
                    val portfolio = Portfolio(
                        id = resultSet.getString("id"),
                        user_id = resultSet.getString("id_user"),
                        name = resultSet.getString("name"),
                        price = resultSet.getDouble("price"),
                        total_profit = resultSet.getDouble("total_profit"),
                        profitability = resultSet.getDouble("profitability"),
                        change_day = resultSet.getDouble("change_day")
                    )
                    portfolios.add(portfolio)
                }
                resultSet.close()
            }
            portfolios
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
            null
        }
    }

    /***
     * Получение портфелей из базы данных по ID пользователя
     * (Пока нигде не используется)
     */
    fun getPortfolioByUser(userId : Int): List<Portfolio>? {
        val portfolios = mutableListOf<Portfolio>()
        return try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, id_user, name, price, total_profit,
                | profitability, change_day  FROM public."Portfolio" WHERE id_user = ${userId} """)
            resultSet?.let {
                while (resultSet.next()) {
                    val portfolio = Portfolio(
                        id = resultSet.getString("id"),
                        user_id = resultSet.getString("id_user"),
                        name = resultSet.getString("name"),
                        price = resultSet.getDouble("price"),
                        total_profit = resultSet.getDouble("total_profit"),
                        profitability = resultSet.getDouble("profitability"),
                        change_day = resultSet.getDouble("change_day")
                    )
                    portfolios.add(portfolio)
                }
                resultSet.close()
            }
            portfolios
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
            null
        }
    }

    /***
     * Удаление портфеля из базы данных по ID
     */
    fun deletePortfolio(id : Int) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(""" DELETE FROM "Portfolio" WHERE id = ${id} """)
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }
}

