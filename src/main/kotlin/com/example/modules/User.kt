package com.example.modules

import kotlinx.serialization.Serializable

/***
 * Образ сущности "Пользователь" из базы данных
 */
@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val create_date: String
)

val users = mutableListOf<User>()

 // TODO save user id once logged in?
