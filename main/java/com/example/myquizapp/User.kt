package com.example.myquizapp

data class User(
    val id: Int,
    val fullName: String,
    val emailOrPhone: String,
    val password: String,
    val role: String
)
