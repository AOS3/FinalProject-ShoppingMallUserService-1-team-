package com.example.frume.model

import com.google.firebase.Timestamp

// 정기 구독 Model
class SubscribeModel {
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
    var subscribeCycleWeek = ""

    // 배송 반복 주기 2
    var subscribeCycleDay = ""

    // 구독상태
    var subscribeState = 0

    // 구독 날짜
    var subscribeTimeStamp = Timestamp.now()


}