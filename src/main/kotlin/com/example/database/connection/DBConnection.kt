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

        /***
         * Следующие функции получают всё, что есть в базе данных
         * и красиво выводят.
         * (пользователей, портфели, компании, акции)
         *
         * (Получают в принципе, выводят красиво ДЛЯ НАС)
         */

        userString(userQueries.getUsers())
        portfolioString(portfolioQueries.getPortfolios())
        companyString(companyQueries.getCompanies())
        stockString(stockQueries.getStocks())

        /***
         * Следующие функции добавляют в базу данных новую информацию
         * (пользователей, портфели, компании, акции)
         */

        userQueries.addUser("asd", "asd", "hii@asd.ru")
        portfolioQueries.addPortfolio("55", "1")
        companyQueries.addCompany("asdf", 787845.0)
        stockQueries.addStock("1", "16",  5, "st", 9898.0, 7878.0)

        /***
         * Следующие функции выполняют поиск объектов
         * (пользователей, портфели, компании, акции)
         * в базе данных по ID
         */

        userString(userQueries.getUserByID(1))
        portfolioString(portfolioQueries.getPortfolioByID(1))
        stockString(stockQueries.getStockByID(1))
        companyString(companyQueries.getCompanyByID(1))

        /***
         * Следующие функции выполняют авторизацию пользователя
         * (введенный пароль сравнивается с паролем от пользователя в базе данных)
         * и обновление пароля
         */

        userQueries.authorization("iamcool", "hii@mail.ru")
        userQueries.updatePassword("evencooler", "hii@mail.ru")

        /***
         * Следующие функции выполняют удаление объектов
         * (пользователей, портфели, компании, акции)
         * из базы данных по ID
         */

        userQueries.deleteUser(1)
        portfolioQueries.deletePortfolio(1)
        stockQueries.deleteStock(1)
        companyQueries.deleteCompany(1)

    } else {
        println(errorMessage)
    }
}


/***
 * В следующем классе выполняется подключение к базе данных PostgreSQL.
 * В случае неудачи функции в main не будут выполнены.
 */
class ConnectionDB {

    private var connection: Connection? = null
    init {
        try {
            connection = DriverManager.getConnection(url, username, DBPassword)
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


