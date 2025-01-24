package com.example.frume.fragment.user_fragment.user_cart

import com.example.frume.data_ye.TempCartProduct

interface CartClickListener {
    fun onClickAdd(pos: Int, cartProduct: TempCartProduct)
    fun onClickMinus(pos: Int, cartProduct: TempCartProduct)
    fun onClickCheckBox()
    fun onClickItemCheckBox(pos: Int, cartProduct: TempCartProduct)
}