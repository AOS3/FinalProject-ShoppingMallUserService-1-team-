package com.example.frume.data

import android.os.Parcelable
import com.example.frume.util.CategoryType
import kotlinx.parcelize.Parcelize


// 값을 보내고 싶을 때 safeArgs
@Parcelize
data class TempProduct(
    val productImgResourceId: Int,
    val productName: String,
    val productPrice: Int,
    val productDescription: String,
    val productCategory: CategoryType
) : Parcelable