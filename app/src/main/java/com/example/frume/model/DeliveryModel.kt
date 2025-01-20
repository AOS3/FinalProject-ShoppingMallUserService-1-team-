package com.example.frume.model

import com.example.frume.util.DeliveryState
import com.example.frume.util.DeliverySubscribeState
import com.google.firebase.Timestamp
import java.sql.Time

// 배송 Model
class DeliveryModel {
    // 배송 문서 ID
    var deliveryDocId = ""

    // 주문 ID
    var deliveryOrderDocId = ""

    // 배송지 문서 ID
    var deliveryAddressDocId = ""

    // 배송 방식
    var deliveryOption = ""

    // 배송 예정일
    var deliveryDueDate = Timestamp.now()

    // 정기배송여부
    var deliveryIsSubscribed = DeliverySubscribeState.DELIVERY_STATE_NOT_SUBSCRIBE // 0 : 비구독

    // 기타사항
    var deliveryEtc = ""

    // 등록시간
    var deliveryTimeStamp = Timestamp.now()

    // 배송 상태
    var deliveryState = DeliveryState.DELIVERY_STATE_READY_FOR_SHIPMENT // 1 : 출고 준비중

}