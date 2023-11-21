package com.example.modules

import kotlinx.serialization.Serializable

/***
 * Образ сущности "Портфель" из базы данных
 */
@Serializable
data class Portfolio (
    val id: String,
    val user_id: String,
    val name: String,
    val price: Double,
    val total_profit: Double,
    val profitability: Double,
    val change_day: Double
)

val portfolios = mutableListOf<Portfolio>()

