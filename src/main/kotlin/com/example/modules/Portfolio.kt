package com.example.modules

import kotlinx.serialization.Serializable

@Serializable
data class Portfolio (
    val id: String,
    val user_id: String,
    val name: String
)

val portfolios = mutableListOf<Portfolio>()

