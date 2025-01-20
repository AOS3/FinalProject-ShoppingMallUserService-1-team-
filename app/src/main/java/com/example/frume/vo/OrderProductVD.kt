package com.example.frume.vo

import com.example.frume.util.OrderProductState
import com.google.firebase.Timestamp

class OrderProductVD {
    // 주문 상품 ID
    var orderProductDocId = ""

    // 주문 회원 ID
    var orderCustomerDocId = ""

    // 주문 ID
    var orderId = ""

    // 상품 이름
    var orderProductName = ""

    // 상품 가격
    var orderProductPrice = 0

    // 상품 수량
    var orderProductCount = 0

    // 상품 이미지
    var orderProductImagePath = ""

    // 적립금 사용값
    var orderProductReward = 0

    // 배송비
    var orderProductDeliveryCost = 0

    // 최종 가격
    var orderProductTotalPrice = 0

    // 상태
    var orderProductState = 1 // 1 : 정상

    // 등록시간
    var orderProductTimeStamp = Timestamp.now()

    // 배송된 날짜
    var orderProductDeliveryCompletedDate = Timestamp.now()
}