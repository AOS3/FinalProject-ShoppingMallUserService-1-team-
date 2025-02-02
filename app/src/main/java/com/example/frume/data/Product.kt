package com.example.frume.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var productCategory1: String,
    var productCategory2: String,
    var productCategory3: String?,
    var productDescription: String,
    var productDocId: String,
    var productImages: List<String>,
    var productName: String,
    var productPrice: Int,
    var productSalesCount: Int,
    var productSellingState: Int,
    var productTimeStamp: Timestamp,
    var productType: Int,
    var productUnit: String,
    var productVariety: String,
    var productVolume: Int
) : Parcelable {
    constructor() : this(
        "", "", "", "",
        "", listOf(), "", 0, 0,
        1, Timestamp.now(), 1, "", "", 0
    )
}