package com.example.frume.vo

import com.example.frume.util.DeliveryCycleDays
import com.example.frume.util.DeliveryCycleWeeks
import com.google.firebase.Timestamp

class CartProductVO {
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
    var cartItemIsSubscribed = false // 0 : 비구독

    // 배송 예정일
    var cartItemDeliveryDueDate = Timestamp.now()

    // 배송 반복 주기1
    var cartItemDeliveryCycleWeek = 1 // 1주

    // 배송 반복 주기2
    var cartItemDeliveryCycleDay = 1 // 월요일

    // 등록시간
    var cartItemDeliveryTimeStamp = Timestamp.now()

    // 구매 여부( 구매 할 물건인가?)
    var cartItemIsPurchases = true // 구매할 품목임

    // 상태
    var cartProductState = 0 // 정상
}