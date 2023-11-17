package com.example
import com.example.modules.Company
import com.example.modules.User
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


fun main() {
    val connectionDB = ConnectionDB()
    if (connectionDB.isDatabaseConnected()) {

        val companies = connectionDB.getCompanyFromDatabase()
        companies?.forEach{ company ->
            println("Id: ${company.id}, " +
                    "Name: ${company.name}, " +
                    "Email: ${company.current_price}"
            )
        }

        val users = connectionDB.getUsersFromDatabase()
        users?.forEach { user ->
            println("Id: ${user.id}, " +
                    "Name: ${user.name}, " +
                    "Email: ${user.email}, " +
                    "Password: ${user.password}, " +
                    "Create date: ${user.create_date}")
        }
        println(users)

    } else {
        println("Ошибка при подключении к базе данных")
    }
}

class ConnectionDB {
    private val url = "jdbc:postgresql://localhost:5432/investment"
    private val username = "postgres"
    private val password = "200319792003saa2003"
    private var connection: Connection? = null

    init {
        try {
            connection = DriverManager.getConnection(url, username, password)
            if (connection != null) {
                println("Соединение с базой данных установлено")
            }
        } catch (e: SQLException) {
            println("Ошибка при установлении соединения с базой данных: ${e.message}")
        }
    }

    fun isDatabaseConnected(): Boolean {
        return connection != null
    }

    fun getCompanyFromDatabase(): List<Company>? {
        val companies = mutableListOf<Company>()
        return try{
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id_company, name, current_price FROM public."Company"""")
            resultSet?.let {
                while (resultSet.next()) {
                    val company = Company(
                        id = resultSet.getString("id_company"),
                        name = resultSet.getString("name"),
                        current_price = resultSet.getFloat("current_price")
                    )
                    companies.add(company)
                }
                resultSet.close()
            }
            companies
        }catch (e: SQLException) {
            println("Ошибка при выполнении запроса: ${e.message}")
            null
        }
    }


    fun getUsersFromDatabase(): List<User>? {
        val users = mutableListOf<User>()
        return try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, name, email, id_password, create_date FROM public."ref_User"""")
            resultSet?.let {
                while (resultSet.next()) {
                    val user = User(
                        id = resultSet.getString("id"),
                        name = resultSet.getString("name"),
                        email = resultSet.getString("email"),
                        password = resultSet.getString("id_password"),
                        create_date = resultSet.getString("create_date")
                    )
                    users.add(user)
                }
                resultSet.close()
            }
            users
        } catch (e: SQLException) {
            println("Ошибка при выполнении запроса: ${e.message}")
            null
        }
    }


}