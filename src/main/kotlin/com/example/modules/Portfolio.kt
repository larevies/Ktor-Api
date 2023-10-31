package com.example.modules

import kotlinx.serialization.Serializable

/*data class Stock(
    val id : Int,
    val id_portfolio : Int,
    val id_company : Int,
    val id : Int,
    val id : Int,
    val id : Int,
    val id : Int,
    val id : Int,
    val id : Int,
    val id : Int,
    val id : Int,
    val id : Int
)*/

@Serializable
data class Portfolio (
    val id: Int


)

val portfolios = mutableListOf<Portfolio>();
