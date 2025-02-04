package com.example.frume.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDone(
    val payment: String,
    val totalPrice: Int,
    val paymentDate: String
) : Parcelable