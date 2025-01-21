package com.example.frume.model

import com.example.frume.util.DeliveryCycleDays
import com.example.frume.util.DeliveryCycleWeeks
import com.example.frume.util.DeliverySubscribeState
import com.example.frume.util.SubscribeState
import com.example.frume.vo.SubscribeVO
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
    var deliveryCycleWeeks = DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_ONE // 1 : 1주

    // 배송 반복 주기 2
    var deliveryCycleDays = DeliveryCycleDays.DELIVERY_CYCLE_DAYS_MONDAY // 1 : 월요일

    // 구독상태
    var subscribeState = SubscribeState.SUBSCRIBE_STATE_NOT_SUBSCRIBE // 0 : 비구독

    // 구독 날짜
    var subscribeTimeStamp = Timestamp.now()



    fun toSubscribeVO(): SubscribeVO{
        val subscribeVO = SubscribeVO()
        subscribeVO.subscribeDocId =subscribeDocId
        subscribeVO.customerDocId =customerDocId
        subscribeVO.productDocId =productDocId
        subscribeVO.orderDocId =orderDocId
        subscribeVO.subscribeProductCount =subscribeProductCount
        subscribeVO.deliveryCycleWeeks =deliveryCycleWeeks.num
        subscribeVO.deliveryCycleDays =deliveryCycleDays .num
        subscribeVO.subscribeState =subscribeState.num
        subscribeVO.subscribeTimeStamp = subscribeTimeStamp

        return subscribeVO
    }


}