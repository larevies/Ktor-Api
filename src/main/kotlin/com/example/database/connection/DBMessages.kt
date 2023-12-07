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
                "Create date: ${user.createDate}")}
    println()
}

fun portfolioString (portfolios : List<Portfolio>?) {
    portfolios?.forEach{ portfolio ->
        println("Id: ${portfolio.id}, " +
                "Owner: ${portfolio.userId}, " +
                "Name: ${portfolio.name}, " +
                "price: ${portfolio.price}, " +
                "total profit: ${portfolio.totalProfit}, " +
                "profitability: ${portfolio.profitability}, " +
                "change day: ${portfolio.changeDay}")}
    println()
}

fun stockString (stocks : List<Stock>?) {
    stocks?.forEach{ stock ->
        println("Id: ${stock.id}, " +
                "Portfolio: ${stock.idPortfolio}, " +
                "Company: ${stock.idCompany}, " +
                "Name: ${stock.name}, " +
                "Current price: ${stock.currentPrice}, " +
                "Purchase price: ${stock.purchasePrice}")}
    println()
}

fun companyString (companies : List<Company>?) {
    companies?.forEach{ company ->
        println("Id: ${company.id}, " +
                "Name: ${company.name}, " +
                "Current price: ${company.currentPrice}")}
    println()
}