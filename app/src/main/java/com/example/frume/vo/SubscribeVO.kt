package com.example.frume.vo

import com.example.frume.util.DeliveryCycleDays
import com.example.frume.util.DeliveryCycleWeeks
import com.example.frume.util.SubscribeState
import com.google.firebase.Timestamp

class SubscribeVO {
    // 구독 문서 ID
    var subscribeDocId = ""

    // 회원 문서 ID
    var customerDocId = ""

    // 상품 문서 ID
    var productDocId = ""

    // 주문 문서 ID
    var orderDocId = ""

    // 수량
    var subscribeProductCount = 0

    // 배송 반복 주기 1
    var deliveryCycleWeeks = 1 // 1 : 1주

    // 배송 반복 주기 2
    var deliveryCycleDays = 1 // 1 : 월요일

    // 구독상태
    var subscribeState = 0 // 0 : 비구독

    // 구독 날짜
    var subscribeTimeStamp = Timestamp.now()

}