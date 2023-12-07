package com.example.database.queries

import com.example.database.connection.*
import com.example.modules.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


/***
 * Следующий класс содержит функции, выполняющие SQL запросы в базу данных к таблице "Пользователи".
 */

class UserQueries {

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
     * Добавление пользователя в базу данных
     */
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


    /***
     * Получение всех пользователей из базы данных
     */
    fun getUsers(): List<User>? {
        val users = mutableListOf<User>()
        return try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("""SELECT id, name, email, id_password, create_date FROM public."ref_User"""")
            resultSet?.use {
                while (resultSet.next()) {
                    val user = User(
                        id = resultSet.getString("id"),
                        name = resultSet.getString("name"),
                        email = resultSet.getString("email"),
                        password = resultSet.getString("id_password"),
                        createDate = resultSet.getString("create_date")
                    )
                    users.add(user)
                }
            }
            users
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
            null
        }
    }

    /***
     * Получение пользователя по ID из базы данных
     */
    fun getUserByID(id: Int?): User? {
        var user: User? = null

        try {
            val statement = connection?.prepareStatement(
                """SELECT id, name, email, id_password, create_date
               FROM public."ref_User" 
               WHERE id = ? """
            )
            statement?.setInt(1, id ?: 0)

            val resultSet = statement?.executeQuery()

            resultSet?.use {
                if (resultSet.next()) {
                    user = User(
                        id = resultSet.getString("id"),
                        name = resultSet.getString("name"),
                        email = resultSet.getString("email"),
                        password = resultSet.getString("id_password"),
                        createDate = resultSet.getString("create_date")
                    )
                }
            }
        } catch (e: SQLException) {
            println("Error executing SQL query: ${e.message}")
        }
        return user
    }

    /***
     * Удаление пользователя по ID из базы данных
     */
    fun deleteUser(id : Int) {
        try {
            val statement = connection?.createStatement()
            statement?.executeQuery(""" DELETE FROM "ref_User" WHERE id = ${id} """)
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
    }


    /***
     * Авторизация пользователя
     * (Сравнение введенного пароля и логина с паролем и логином из базы данных)
     */
    fun authorization (userPassword:String, email: String): Boolean {
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
            resultSet?.use {
                if (resultSet.next().toString() != "") {
                    println(successfulAuthorization)
                    return true
                } else {
                    println(failedAuthorization)
                    return false
                }
            }
        } catch (e: SQLException) {
            println(queryError)
            println("${e.message}")
        }
        return false
    }

    /***
     * Обновление пароля
     * (Замена старого пароля на новый в базе данных)
     */
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