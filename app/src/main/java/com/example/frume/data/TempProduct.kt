package com.example.frume.data

import android.os.Parcelable
import com.example.frume.util.CategoryType
import kotlinx.parcelize.Parcelize


// 값을 보내고 싶을 때 safeArgs
@Parcelize
data class TempProduct(
    val productDocId: String,
    val productImgResourceId: Int,
    val productName: String,
    val productPrice: Int,
    val productDescription: String,

    // 원래 타입이 CategoryType 이거였는데 일단 임시방편으로 String으로 하고
    // DB에 있는 카테고리 중 하나와 연결
    val productCategory: String // 홈,특가, ..
) : Parcelable