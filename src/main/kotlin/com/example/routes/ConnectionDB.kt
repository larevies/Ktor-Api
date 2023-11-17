package com.example.routes
import com.example.modules.Company
import com.example.modules.User
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/*
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

        connectionDB.authorization("newPaSsWoRd", "э15051950@mail.ru")

        //println(users)
        //connectionDB.addAUser("asdfgasdggahs", "helloworld", "15051950@mail.ru")
        // connectionDB.updatePassword("newPaSsWoRd", "15051950@mail.ru")


    } else {
        println("Ошибка при подключении к базе данных")
    }
}*/

public class ConnectionDB {
    private val url = "jdbc:postgresql://localhost:5432/investment"
    private val username = "postgres"
    private val password = "lab2itmo"
    private var connection: Connection? = null
//  private val password = "200319792003saa2003"
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
            val resultSet = statement?.executeQuery("""SELECT id, name, current_price FROM public."Company"""")
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
        } catch (e: SQLException) {
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

    fun addAUser(password : String, login : String, email : String) {

        try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery(
                """WITH new_password AS(
                        INSERT INTO public."ref_Password" (password)
                        VALUES (crypt('${password}', gen_salt('md5')))
                        RETURNING id AS id_password
                        )
                    INSERT INTO public."ref_User" (name, email, create_date, id_password)
                    SELECT '${login}', '${email}', NOW(), id_password
                    FROM new_password;
                    """
            )
        } catch (e: SQLException) {
            println("Ошибка при выполнении запроса: ${e.message}")
        }
    }

    fun authorization (user_password:String, email: String) {
        try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery(
                """WITH search_password AS(
                            SELECT password FROM public."ref_Password"
                            WHERE id = (SELECT id_password from public."ref_User" WHERE email='${email}')
                        )
                        SELECT 
                            CASE
                                WHEN password = crypt('${user_password}', password)
                                    THEN 'SUCCESSFUL'
                            END 
                            FROM search_password

                    """
            )
            if (resultSet != null) {
                val isNotEmpty = resultSet.next()
                if (isNotEmpty) {
                    println("sorry your password is wrong")
                } else {
                    println("welcome")
                }
            }
        } catch (e: SQLException) {
            println("Ошибка при выполнении запроса: ${e.message}")
        }
    }

    fun updatePassword (new_password: String, email: String) {
        try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery(
                """WITH update_password AS(
                            SELECT password FROM public."ref_Password"
                            WHERE id = (SELECT id_password from public."ref_User" WHERE email='${email}')
                        )
                        
                        UPDATE public."ref_Password"
                        SET password = crypt('${new_password}', gen_salt('md5')) FROM update_password;
                    """
            )
        } catch (e: SQLException) {
            println("Ошибка при выполнении запроса: ${e.message}")
        }
    }
}


// (registration) add user
//