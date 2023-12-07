package com.example.modules

import kotlinx.serialization.Serializable

/***
 * Образ сущности "Портфель" из базы данных
 */
@Serializable
data class Portfolio (
    val id: String? = null,
    val userId: String,
    val name: String,
    val price: Double,
    val totalProfit: Double,
    val profitability: Double,
    val changeDay: Double
)


