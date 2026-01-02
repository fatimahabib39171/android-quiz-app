package com.example.myquizapp.models

data class Question(
    val id: Int = 0,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
