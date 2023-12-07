package com.example.modules

import kotlinx.serialization.Serializable

/***
 * Образ сущности "Акция" из базы данных
 */
@Serializable
data class Stock(
    val id: String? = null,
    val idPortfolio: String,
    val idCompany: String,
    val amount: Int,
    val name : String,
    val currentPrice: Double,
    val purchasePrice: Double
)