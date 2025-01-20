package com.example.frume.vo

import com.google.firebase.Timestamp

class DeliveryAddressVO {
    // 배송지 문서 id
    var deliveryAddressDocId = ""

    // 사용자 문서 id
    var deliveryAddressUserDocId = ""

    // 배송지 이름
    var deliveryAddressName = ""

    // 기본 주소
    var deliveryAddressBasicAddress = ""

    // 상세 주소
    var deliveryAddressDetailAddress = ""

    // 우편번호
    var deliveryAddressPostNumber = ""

    // 배송지 휴대폰번호
    var deliveryAddressPhoneNumber = ""

    // 고정 배송지 여부
    var deliveryAddressIsDefaultAddress = false // false : 일반 배송지

    // 등록시간
    var deliveryAddressTimeStamp = Timestamp.now()

    // 상태
    var deliveryAddressState = 0 // 0: 정상
}