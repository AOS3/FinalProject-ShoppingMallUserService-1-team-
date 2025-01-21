package com.example.frume.vo

import com.example.frume.model.SubscribeModel
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


    fun toSubscribeModel(): SubscribeModel {
        val subscribeModel = SubscribeModel()
        subscribeModel.subscribeDocId =subscribeDocId
        subscribeModel.customerDocId =customerDocId
        subscribeModel.productDocId =productDocId
        subscribeModel.orderDocId =orderDocId
        subscribeModel.subscribeProductCount =subscribeProductCount
        subscribeModel.subscribeTimeStamp = subscribeTimeStamp
        when(deliveryCycleWeeks){
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_ONE.num->{DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_ONE}
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_TWO.num->{DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_TWO}
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_THREE.num->{DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_THREE}
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_FOUR.num->{DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_FOUR}
        }
        when(deliveryCycleDays){
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_MONDAY.num->{DeliveryCycleDays.DELIVERY_CYCLE_DAYS_MONDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_TUESDAY.num->{DeliveryCycleDays.DELIVERY_CYCLE_DAYS_TUESDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_WEDNESDAY.num->{DeliveryCycleDays.DELIVERY_CYCLE_DAYS_WEDNESDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_THURSDAY.num->{DeliveryCycleDays.DELIVERY_CYCLE_DAYS_THURSDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_FRIDAY.num->{DeliveryCycleDays.DELIVERY_CYCLE_DAYS_FRIDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SATURDAY.num->{DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SATURDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SUNDAY.num->{DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SUNDAY}
        }
        when(subscribeState){
            SubscribeState.SUBSCRIBE_STATE_NOT_SUBSCRIBE.num->{subscribeModel.subscribeState = SubscribeState.SUBSCRIBE_STATE_NOT_SUBSCRIBE}
            SubscribeState.SUBSCRIBE_STATE_SUBSCRIBE.num->{subscribeModel.subscribeState = SubscribeState.SUBSCRIBE_STATE_SUBSCRIBE}

        }

        return subscribeModel
    }

}