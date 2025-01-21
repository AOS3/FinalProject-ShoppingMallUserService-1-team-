package com.example.frume.data

import java.util.Date

data class TempReview(
    val reviewImg: Int,
    val starPoint: Double,
    val reviewDate: Date,
    val reviewTitle: String,
    val reviewBody: String
)