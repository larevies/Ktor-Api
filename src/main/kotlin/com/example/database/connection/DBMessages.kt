package com.example.database.connection

import com.example.modules.Company
import com.example.modules.Portfolio
import com.example.modules.Stock
import com.example.modules.User

const val errorMessage = "Ошибка при установлении соединения с базой данных.\n"
const val successMessage = "Соединение с базой данных установлено.\n"
const val queryError = "Ошибка при выполнении запроса.\n"

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
                "Name: ${portfolio.name}")}
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