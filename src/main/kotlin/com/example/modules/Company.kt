package com.example.modules

import kotlinx.serialization.Serializable
import java.util.Date

/***
 * Образ сущности "Компания" из базы данных
 */
@Serializable
data class Company(
    val id: String,
    val name : String,
    val current_price : Double
)

val companies = mutableListOf<Company>()