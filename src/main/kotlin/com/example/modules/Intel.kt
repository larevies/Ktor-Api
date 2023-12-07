package com.example.modules;

import kotlinx.serialization.Serializable;

@Serializable
data class Intel(
    val password: String,
    val login: String
)

