package com.example.database.connection

/***
 * Этот файл содержит в себе переменные, необходимые для подключения к базе данных PostgreSQL.
 * url - адрес базы данных с портом и названием
 * username - имя пользователя PostgreSQL, по умолчанию - postgres
 * DBPassword - пароль от базы данных PostgreSQL.
 */

const val url = "jdbc:postgresql://localhost:5432/investment"
const val username = "postgres"
const val DBPassword = "lab2itmo"
//const val DBPassword = "200319792003saa2003"