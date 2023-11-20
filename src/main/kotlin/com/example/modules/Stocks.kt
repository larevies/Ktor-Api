package com.example.modules

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val id: String,
    val id_portfolio: String,
    val id_company: String,
    val amount: Int,
    val name : String,
    val current_price: Double,
    val purchase_price: Double
)

val stocks= mutableListOf<Stock>()