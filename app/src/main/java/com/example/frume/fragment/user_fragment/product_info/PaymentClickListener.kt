package com.example.frume.fragment.user_fragment.product_info

import com.example.frume.model.ProductModel


interface PaymentClickListener {
    //fun onClickPaymentBtn(tempProduct: TempProduct)
    fun getProductTemp(): ProductModel
}