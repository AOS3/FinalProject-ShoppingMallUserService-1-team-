package com.example.frume.model

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
    var deliveryIsSubscribed = false

    // 기타사항
    var deliveryEtc = ""

    // 등록시간
    var deliveryTimeStamp = Timestamp.now()

    // 배송 상태
    var deliveryState = ""

}