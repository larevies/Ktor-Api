package com.example.modules

import java.util.Date

data class Company(
    val id: String,
    val name : String,
    val current_price : Double
)

val companies = mutableListOf<Company>()