package com.example.frume.model

import com.google.firebase.Timestamp

// 장바구니 상품 Model

class CartProductModel {
    // 장바구니 상품 문서 ID
    var cartProductDocId = ""

    // 장바구니 문서 ID
    var cartDocId = ""

    // 회원 문서 ID
    var customerDocId = ""

    // 상품 문서 ID
    var cartItemProductDocId = ""

    // 상품 수량
    var cartItemProductQuantity = 0

    // 구독 여부
    var cartItemIsSubscribed = false

    // 배송 예정일
    var cartItemDeliveryDueDate = Timestamp.now()

    // 배송 반복 주기1
    var cartItemDeliveryCycleWeek = ""

    // 배송 반복 주기2
    var cartItemDeliveryCycleDay = ""

    // 등록시간
    var cartItemDeliveryTimeStamp = Timestamp.now()

    // 구매 여부
    var cartItemIsPurchases = 0

    // 상태
    var cartProductState = 0

}