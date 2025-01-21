package com.example.frume.vo

import com.example.frume.model.CartModel
import com.example.frume.model.CartProductModel
import com.example.frume.util.CartState
import com.google.firebase.Timestamp

class CartVO {
    // 장바구니 문서 ID
    var cartDocId = ""

    // 유저 문서 ID
    var customerDocId = ""

    // 등록시간
    var cartDeliveryTimeStamp = Timestamp.now()

    // 노출상태
    var cartDeliveryState = 0 // 정상 : 0, 비정상 : 1

    // 장바구니 items 문서 IDs
    var cartProductItems = mutableListOf<CartProductModel>()

    fun toCartModel(): CartModel {
        val cartModel = CartModel()

        cartModel.cartDocId = cartDocId
        cartModel.customerDocId = customerDocId
        cartModel.cartDeliveryTimeStamp = cartDeliveryTimeStamp
        cartModel.cartProductItems = cartProductItems

        when(cartDeliveryState){
            CartState.CART_STATE_NORMAL.num-> {cartModel.cartDeliveryState = CartState.CART_STATE_NORMAL }
            CartState.CART_STATE_ABNORMAL.num-> {cartModel.cartDeliveryState = CartState.CART_STATE_ABNORMAL }
        }

        return cartModel
    }
}