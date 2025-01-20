package com.example.frume.data_ye

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempCartProduct(
    val imageResId: Int,
    val productName: String,
    val productPrice: Int,
    val quantity: Int
) : Parcelable
