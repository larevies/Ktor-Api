package com.example.modules

import kotlinx.serialization.Serializable

/***
 * Образ сущности "Компания" из базы данных
 */
@Serializable
data class Company(
    val id: String? = null,
    val name : String,
    val current_price : Double
)