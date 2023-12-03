package com.example.database.queries

import com.example.database.connection.*
import com.example.modules.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


/***
 * Следующий класс содержит функции, выполняющие SQL запросы в базу данных к таблице "Акции".
 */
class StockQueries {

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
     * Добавление акции в базу данных
     */
    fun addStock(idPortfolio : String, idCompany : String, amount: Int, name : String,
                 currentPrice : Double, purchasePrice : Double) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(
                """INSERT INTO public."Assets"(id_portfolio, id_company, amount, name, 
                        current_price, purchase_price)
	                    VALUES ('${idPortfolio}', '${idCompany}', ${amount}, '${name}', 
	                    ${currentPrice}, ${purchasePrice}); 
                    """
            )
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }


    /***
     * Получение акции по ID из базы данных
     */
    fun getStockByID(id : Int?): Stock? {
        var stock: Stock? = null

        try {
            val statement = connection?.prepareStatement(
                """SELECT id, id_portfolio, id_company, amount, name, 
                    |current_price, purchase_price 
                    |FROM public."Assets" 
                    |WHERE id = ? """.trimMargin())

            statement?.setInt(1, id ?: 0)

            val resultSet = statement?.executeQuery()

            resultSet?.use {
                if (resultSet.next()) {
                    stock = Stock(
                        id = resultSet.getString("id"),
                        id_portfolio = resultSet.getString("id_portfolio"),
                        id_company = resultSet.getString("id_company"),
                        amount = resultSet.getInt("amount"),
                        name = resultSet.getString("name"),
                        current_price = resultSet.getDouble("current_price"),
                        purchase_price = resultSet.getDouble("purchase_price")
                    )
                    //stocks.add(stock)
                }
                //resultSet.close()
            }
            //stocks
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
        return stock
    }

    /***
     * Получение акции по ID портфеля из базы данных
     */
    fun getStockByPortfolio(idPortfolio : Int): List<Stock>? {
        val stocks = mutableListOf<Stock>()
        return try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, id_portfolio, id_company, amount,
                |                                      name, current_price, purchase_price
	                                                   FROM public."Assets"
                |                                      WHERE id_portfolio = ${idPortfolio}
            """.trimMargin())
            resultSet?.let {
                while (resultSet.next()) {
                    val stock = Stock(
                        id = resultSet.getString("id"),
                        id_portfolio = resultSet.getString("id_portfolio"),
                        id_company = resultSet.getString("id_company"),
                        amount = resultSet.getInt("amount"),
                        name = resultSet.getString("name"),
                        current_price = resultSet.getDouble("current_price"),
                        purchase_price = resultSet.getDouble("purchase_price")
                    )
                    stocks.add(stock)
                }
                resultSet.close()
            }
            stocks
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
            null
        }
    }


    /***
     * Удаление акции из базы данных по ID
     */
    fun deleteStock(id : Int) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(""" DELETE FROM "Assets" WHERE id = ${id} """)
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }
}