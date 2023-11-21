package com.example.database.connection
import com.example.database.queries.CompanyQueries
import com.example.database.queries.PortfolioQueries
import com.example.database.queries.StockQueries
import com.example.database.queries.UserQueries
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

// TODO delete portfolio CASCADE
// TODO delete stock CASCADE

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
        // companyString(companyQueries.getCompanies())
        // stockString(stockQueries.getStocks())


        // добавляем в бд

        //userQueries.addUser("asd", "asd", "hii@asd.ru")
         //portfolioQueries.addPortfolio("55", "1")

        // stockQueries.addStock("1", "1",  5, "st", 9898.0, 7878.0)
        // companyQueries.addCompany("asdf", 787845.0)


        // ищем с конкретикой (разные айди)

        //userString(userQueries.getUserByID(1))

        // portfolioString(portfolioQueries.getPortfolioByID(1))
        // portfolioString(portfolioQueries.getPortfolioByUser(1))

        // stockString(stockQueries.getStockByID(1))
        // stockString(stockQueries.getStockByCompany(1))
        // stockString(stockQueries.getStockByPortfolio(1))

        // companyString(companyQueries.getCompanyByID(1))


        // авторизация и обновление пароля

        // userQueries.authorization("iamcool", "hii@mail.ru")
        // userQueries.updatePassword("evencooler", "hii@mail.ru")


        // удалить

        // userQueries.deleteUser(11)
        // portfolioQueries.deletePortfolio(1)
        // stockQueries.deleteStock(1)
        // companyQueries.deleteCompany(1)

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


