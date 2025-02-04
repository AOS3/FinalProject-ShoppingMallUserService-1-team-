package com.example.frume.listener

import com.example.frume.model.CartProductModel

interface CartClickListener {
    fun onClickCheckBoxAll()
    fun onClickItemCheckBox(pos: Int, cartProduct: CartProductModel)
}