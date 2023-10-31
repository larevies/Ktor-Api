package com.example.modules

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String
)

val users = mutableListOf<User>()


/**
 * save user id once logged in?
 * refer to portfolios by id
 *
 */