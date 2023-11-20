package com.example.database.connection
import com.example.database.queries.CompanyQueries
import com.example.database.queries.PortfolioQueries
import com.example.database.queries.StockQueries
import com.example.database.queries.UserQueries
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

// TODO delete user
// TODO delete portfolio
// TODO delete company
// TODO delete stock

fun main() {
    val connectionDB = ConnectionDB()
    val userQueries = UserQueries()
    val portfolioQueries = PortfolioQueries()
    val stockQueries = StockQueries()
    val companyQueries = CompanyQueries()

    if (connectionDB.isDatabaseConnected()) {


        // получаем всё, что есть в бд

        userString(userQueries.getUsers())
        portfolioString(portfolioQueries.getPortfolios())
        companyString(companyQueries.getCompanies())
        stockString(stockQueries.getStocks())


        // добавляем в бд

        userQueries.addUser("iamcool", "sdff", "hii@sdfsdfsf.ru")
        portfolioQueries.addPortfolio("port", "5", 1515.0, 5656.0, 4545.5, 4545.0)
        stockQueries.addStock("1", "1",  5, "st", 9898.0, 7878.0)
        companyQueries.addCompany("asdf", 787845.0)


        // ищем с конкретикой (разные айди)

        userQueries.getUserByID(1)

        portfolioQueries.getPortfolioByID(1)
        portfolioQueries.getPortfolioByUser(1)

        stockQueries.getStockByID(1)
        stockQueries.getStockByCompany(1)
        stockQueries.getStockByPortfolio(1)

        companyQueries.getCompanyByID(1)


        // авторизация и обновление пароля

        userQueries.authorization("iamcool", "hii@mail.ru")
        userQueries.updatePassword("evencooler", "hii@mail.ru")

    } else {
        println(errorMessage)
    }
}

class ConnectionDB {

    private var connection: Connection? = null
    init {
        try {
            connection = DriverManager.getConnection(url, username, DBpassword)
            if (connection != null) {
                println(successMessage)
            }
        } catch (e: SQLException) {
            println(errorMessage)
            println("${e.message}")
        }
    }
    fun isDatabaseConnected(): Boolean {
        return connection != null
    }
}


