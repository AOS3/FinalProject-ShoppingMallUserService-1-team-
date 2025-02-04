package com.example.frume.listener

import com.example.frume.model.ProductModel


interface PaymentClickListener {
    //fun onClickPaymentBtn(tempProduct: TempProduct)
    fun getProductTemp(): ProductModel
}