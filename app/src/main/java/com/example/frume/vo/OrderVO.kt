package com.example.frume.vo

import com.example.frume.model.CartProductModel
import com.example.frume.util.OrderDeliveryOption
import com.example.frume.util.OrderIsOneTimeDeliveryBoolType
import com.example.frume.util.OrderPaymentOption
import com.example.frume.util.OrderState
import com.google.firebase.Timestamp

class OrderVO {
    // 주문 문서 ID
    var orderDocId = ""

    // 주문 회원 문서 ID
    var orderCustomerDocId = ""

    // 장바구니 속 상품들
    var cartProducts = mutableListOf<CartProductModel>()

    // 배송지ID
    var orderDeliveryAddress = ""

    // 주문자 휴대전화 번호
    var orderCustomerPhoneNumber = ""

    // 결제 방식
    var orderPaymentOption = 1 // 1 : 게좌 이체

    // 배송 방식
    var orderDeliveryOption = 1 // 1: 문 앞 배송

    // 배송 예정일
    var orderDeliveryDueDate = Timestamp.now()

    // 일회성 배송 여부
    var orderIsOneTimeDelivery = true // true : 일회성배송

    // 기타사항
    var orderEtc = ""

    // 주문 날짜
    var orderTimeStamp = Timestamp.now()

    // 주문 상태
    var orderState = 1 // 1 : 결제 대기중
}