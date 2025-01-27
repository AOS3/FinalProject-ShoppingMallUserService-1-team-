package com.example.frume.fragment.user_fragment.user_cart

import com.example.frume.model.CartProductModel

interface CartClickListener {
    fun onClickCheckBoxAll()
    fun onClickItemCheckBox(pos: Int, cartProduct: CartProductModel)
}