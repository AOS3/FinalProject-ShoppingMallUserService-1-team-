package com.example.frume.model

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
    var cartDeliveryState = 0

    // 장바구니 items 문서 IDs
    var cartProductItems = mutableListOf<CartProductModel>()

}

