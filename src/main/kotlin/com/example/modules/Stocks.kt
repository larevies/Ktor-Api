package com.example.modules

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val id: String,
    val id_portfolio: String,
    val id_company: String,
    val name : String,
    val current_price: Float,
    val purchase_price: Float
)

val stocks= mutableListOf<Stock>()