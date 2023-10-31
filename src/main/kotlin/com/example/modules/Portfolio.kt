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
    val id: String,
    val user_id: String,
    val name: String,
    // val total_profit : Float,
    // val change : Float
)

val portfolios = mutableListOf<Portfolio>()


