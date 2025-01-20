package com.example.frume.model

import com.google.firebase.Timestamp

// 주문 Model
class OrderModel {
    // 주문 문서 ID
    var orderDocId = ""

    // 주문 회원 문서 ID
    var orderCustomerDocId = ""

    // 주문 상품 ID들
    var orderProductDocIds = ""

    // 배송지ID
    var orderDeliveryAddress = ""

    // 장바구니 문서 ID
    var cartDocId = ""

    // 주문자 휴대전화 번호
    var orderCustomerPhoneNumber = ""

    // 결제 방식
    var orderPaymentOption = 0

    // 배송 방식
    var orderDeliveryOption = ""

    // 배송 예정일
    var orderDeliveryDueDate = Timestamp.now()

    // 정기배송여부
    var orderIsSubscribed = false

    // 기타사항
    var orderEtc = ""

    // 주문 날짜
    var orderTimeStamp = Timestamp.now()

    // 주문 상태
    var orderState = 0

}