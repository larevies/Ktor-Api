package com.example.database.connection

import com.example.modules.Company
import com.example.modules.Portfolio
import com.example.modules.Stock
import com.example.modules.User

/***
 * Код из этого файла необходим для выведения информации на экран.
 * Строковые константы выводятся при успехе/неудаче работы с базой данных.
 * Функции необходимы для читаемого вывода полученной информации на экран.
 */

const val errorMessage = "Ошибка при установлении соединения с базой данных.\n"
const val successMessage = "Соединение с базой данных установлено.\n"
const val queryError = "Ошибка при выполнении запроса.\n"
const val successfulAuthorization = "Добро пожаловать в аккаунт!"
const val failedAuthorization = "Пароль или имя пользователя указаны неверно."

fun userString (users : List<User>?) {
    users?.forEach{ user ->
        println("Id: ${user.id}, " +
                "Name: ${user.name}, " +
                "Email: ${user.email}, " +
                "Password: ${user.password}, " +
                "Create date: ${user.create_date}")}
    println()
}

fun portfolioString (portfolios : List<Portfolio>?) {
    portfolios?.forEach{ portfolio ->
        println("Id: ${portfolio.id}, " +
                "Owner: ${portfolio.user_id}, " +
                "Name: ${portfolio.name}, " +
                "price: ${portfolio.price}, " +
                "total profit: ${portfolio.total_profit}, " +
                "profitability: ${portfolio.profitability}, " +
                "change day: ${portfolio.change_day}")}
    println()
}

fun stockString (stocks : List<Stock>?) {
    stocks?.forEach{ stock ->
        println("Id: ${stock.id}, " +
                "Portfolio: ${stock.id_portfolio}, " +
                "Company: ${stock.id_company}, " +
                "Name: ${stock.name}, " +
                "Current price: ${stock.current_price}, " +
                "Purchase price: ${stock.purchase_price}")}
    println()
}

fun companyString (companies : List<Company>?) {
    companies?.forEach{ company ->
        println("Id: ${company.id}, " +
                "Name: ${company.name}, " +
                "Current price: ${company.current_price}")}
    println()
}