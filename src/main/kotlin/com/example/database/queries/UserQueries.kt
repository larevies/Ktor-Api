package com.example.database.queries

import com.example.database.connection.*
import com.example.modules.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

const val successfulAuthorization = "Добро пожаловать в аккаунт!"
const val failedAuthorization = "Пароль или имя пользователя указаны неверно."

class UserQueries {

    private var connection: Connection? = null


    init {
        try {
            connection = DriverManager.getConnection(url, username, DBpassword)

        } catch (e: SQLException) {
            println(errorMessage)
            println("${e.message}")
        }
    }


    fun addUser(password : String, login : String, email : String) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(
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
            println(queryError)
            println("${e.message}")
        }
    }


    fun getUsers(): List<User>? {
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
            println(queryError)
            println("${e.message}")
            null
        }
    }


    fun getUserByID(id : Int): List<User>? {
        val users = mutableListOf<User>()
        return try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery(
                """SELECT id, name, email, id_password, create_date 
                |                                      FROM public."ref_User" 
                |                                      WHERE id = ${id} 
            """.trimMargin()
            )
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
            println(queryError)
            println("${e.message}")
            null
        }
    }

    fun deleteUser(id : Int) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(""" DELETE FROM "ref_User" WHERE id = ${id} """)
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }


    fun authorization (userPassword:String, email: String) {
        try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery(
                """WITH search_password AS(
                            SELECT password FROM public."ref_Password"
                            WHERE id = (SELECT id_password from public."ref_User" WHERE email='${email}')
                        )
                        SELECT 
                            CASE
                                WHEN password = crypt('${userPassword}', password)
                                    THEN 'SUCCESSFUL'
                            END 
                            FROM search_password

                    """
            )
            if (resultSet != null) {
                val isNotEmpty = resultSet.next()
                if (isNotEmpty) {
                    println(failedAuthorization)
                } else {
                    println(successfulAuthorization)
                }
            }
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }


    fun updatePassword (newPassword: String, email: String) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(
                """WITH update_password AS(
                            SELECT password FROM public."ref_Password"
                            WHERE id = (SELECT id_password from public."ref_User" WHERE email='${email}')
                        )
                        
                        UPDATE public."ref_Password"
                        SET password = crypt('${newPassword}', gen_salt('md5')) FROM update_password;
                    """
            )
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }
}