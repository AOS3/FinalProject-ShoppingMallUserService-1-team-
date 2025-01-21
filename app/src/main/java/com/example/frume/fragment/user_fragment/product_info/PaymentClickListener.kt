package com.example.frume.fragment.user_fragment.product_info

import com.example.frume.data.TempProduct

interface PaymentClickListener {
    //fun onClickPaymentBtn(tempProduct: TempProduct)
    fun getProductTemp(): TempProduct
}