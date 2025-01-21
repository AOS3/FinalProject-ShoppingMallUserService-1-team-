package com.example.frume.fragment.user_fragment.product_info

import com.example.frume.data.TempReview

interface ReviewClickListener {
    fun onClickProductItem(reView: TempReview)
}