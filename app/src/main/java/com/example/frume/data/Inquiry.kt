package com.example.frume.data

import java.util.Date

data class Inquiry(
    val type: String,
    val title: String,
    val answerState: String,
    val name: String,
    val date: String, // 추후 DATE,
    val secret: Boolean // true => private false => public
)