package com.example.frume.vo

import com.example.frume.model.DeliveryAddressModel
import com.example.frume.util.DeliveryAddressState
import com.example.frume.util.DeliveryDefaultAddressBoolType
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
    var deliveryAddressState = 1 // 1: 정상

    // 받는사람 이름
    var deliveryAddressReceiverName = ""

    fun toDeliverAddressModel(): DeliveryAddressModel {
        val deliveryAddressModel = DeliveryAddressModel()

        deliveryAddressModel.deliveryAddressDocId = deliveryAddressDocId
        deliveryAddressModel.deliveryAddressUserDocId = deliveryAddressUserDocId
        deliveryAddressModel.deliveryAddressName = deliveryAddressName
        deliveryAddressModel.deliveryAddressBasicAddress = deliveryAddressBasicAddress
        deliveryAddressModel.deliveryAddressDetailAddress = deliveryAddressDetailAddress
        deliveryAddressModel.deliveryAddressPostNumber = deliveryAddressPostNumber
        deliveryAddressModel.deliveryAddressPhoneNumber = deliveryAddressPhoneNumber
        deliveryAddressModel.deliveryAddressReceiverName=deliveryAddressReceiverName
        when(deliveryAddressIsDefaultAddress){
            DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_NOT_DEFAULT.bool->{deliveryAddressModel.deliveryAddressIsDefaultAddress= DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_NOT_DEFAULT}
            DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_DEFAULT.bool->{deliveryAddressModel.deliveryAddressIsDefaultAddress= DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_DEFAULT}
            else->{}

        }
        deliveryAddressModel.deliveryAddressTimeStamp = deliveryAddressTimeStamp
        when(deliveryAddressState){
            DeliveryAddressState.DELIVERY_ADDRESS_STATE_NORMAL.num->{deliveryAddressModel.deliveryAddressState=DeliveryAddressState.DELIVERY_ADDRESS_STATE_NORMAL}
            DeliveryAddressState.DELIVERY_ADDRESS_STATE_ABNORMAL.num->{deliveryAddressModel.deliveryAddressState=DeliveryAddressState.DELIVERY_ADDRESS_STATE_ABNORMAL}
        }

        return deliveryAddressModel

    }
}