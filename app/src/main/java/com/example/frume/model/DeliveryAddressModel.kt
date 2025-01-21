package com.example.frume.model

import com.example.frume.util.DeliveryAddressState
import com.example.frume.util.DeliveryDefaultAddressBoolType
import com.example.frume.vo.DeliveryAddressVO
import com.google.firebase.Timestamp

// 배송지 Model
class DeliveryAddressModel {
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
    var deliveryAddressIsDefaultAddress =
        DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_NOT_DEFAULT // false : 일반 배송지

    // 등록시간
    var deliveryAddressTimeStamp = Timestamp.now()

    // 상태
    var deliveryAddressState = DeliveryAddressState.DELIVERY_ADDRESS_STATE_NORMAL // 1: 정상

    fun toDeliverAddressVO(): DeliveryAddressVO {
        val deliveryAddressVO = DeliveryAddressVO()

        deliveryAddressVO.deliveryAddressDocId = deliveryAddressDocId
        deliveryAddressVO.deliveryAddressUserDocId = deliveryAddressUserDocId
        deliveryAddressVO.deliveryAddressName = deliveryAddressName
        deliveryAddressVO.deliveryAddressBasicAddress = deliveryAddressBasicAddress
        deliveryAddressVO.deliveryAddressDetailAddress = deliveryAddressDetailAddress
        deliveryAddressVO.deliveryAddressPostNumber = deliveryAddressPostNumber
        deliveryAddressVO.deliveryAddressPhoneNumber = deliveryAddressPhoneNumber
        deliveryAddressVO.deliveryAddressIsDefaultAddress = deliveryAddressIsDefaultAddress.bool
        deliveryAddressVO.deliveryAddressTimeStamp = deliveryAddressTimeStamp
        deliveryAddressVO.deliveryAddressState = deliveryAddressState.num

        return deliveryAddressVO

    }

}