package com.example.modules

import java.util.Date

data class Currency(
    val id: String,
    val name : String,
    val date : Date,
    val value : Float
)

val currencies = mutableListOf<Currency>()