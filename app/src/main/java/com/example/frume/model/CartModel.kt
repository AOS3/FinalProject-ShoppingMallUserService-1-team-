package com.example.frume.model

import com.example.frume.util.CartState
import com.example.frume.vo.CartVO
import com.google.firebase.Timestamp

// 장바구니 Model
class CartModel {
    // 장바구니 문서 ID
    var cartDocId = ""

    // 유저 문서 ID
    var customerDocId = ""

    // 등록시간
    var cartDeliveryTimeStamp = Timestamp.now()

    // 노출상태
    var cartDeliveryState = CartState.CART_STATE_NORMAL // 정상 : 1, 비정상 : 2

    // 장바구니 items 문서 IDs
    var cartProductItems = mutableListOf<CartProductModel>()

    fun toCartVO(): CartVO {
        val cartVo = CartVO()

        cartVo.cartDocId = cartDocId
        cartVo.customerDocId = customerDocId
        cartVo.cartDeliveryTimeStamp = cartDeliveryTimeStamp
        cartVo.cartDeliveryState = cartDeliveryState.num
        cartVo.cartProductItems = cartProductItems

        return cartVo
    }

}

