package com.example.frume.data_ye

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempAddress(
    val addressName: String,
    val addressDetail: String,
    val postalCode: String,
    val addressIconResId: Int
) : Parcelable
