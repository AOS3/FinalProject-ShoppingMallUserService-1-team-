package com.example.frume.data_ye

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempCartProduct(
    val imageResId: Int,
    var productCheck: Boolean=false,
    val productName: String,
    val productPrice: Int,
    var quantity: Int
) : Parcelable
