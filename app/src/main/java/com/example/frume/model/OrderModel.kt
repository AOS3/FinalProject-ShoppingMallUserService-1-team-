package com.example.frume.model

import com.example.frume.util.OrderDeliveryOption
import com.example.frume.util.OrderIsOneTimeDeliveryBoolType
import com.example.frume.util.OrderPaymentOption
import com.example.frume.util.OrderState
import com.example.frume.vo.OrderVO
import com.google.firebase.Timestamp

// 주문 Model
class OrderModel {
    // 주문 문서 ID
    var orderDocId = ""

    // 주문 회원 문서 ID
    var orderCustomerDocId = ""

    // 장바구니 속 상품들
    var cartProducts = mutableListOf<CartProductModel>()

    // 배송지ID
    var orderDeliveryAddress = ""

    // 주문자 휴대전화 번호
    var orderCustomerPhoneNumber = ""

    // 결제 방식
    var orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT // 1 : 게좌 이체

    // 배송 방식
    var orderDeliveryOption = OrderDeliveryOption.DOOR_DELIVERY // 1: 문 앞 배송

    // 배송 예정일
    var orderDeliveryDueDate = Timestamp.now()

    // 일회성 배송 여부
    var orderIsOneTimeDelivery = OrderIsOneTimeDeliveryBoolType.ONE_TIME_DELIVERY // true : 일회성배송

    // 기타사항
    var orderEtc = ""

    // 주문 날짜
    var orderTimeStamp = Timestamp.now()

    // 주문 상태
    var orderState = OrderState.ORDER_STATE_PAYMENT_PENDING // 1 : 결제 대기중


    fun toOrderVO(): OrderVO {

        val orderVO = OrderVO()
        orderVO.orderDocId = orderDocId
        orderVO.orderCustomerDocId = orderCustomerDocId
        orderVO.cartProducts = cartProducts
        orderVO.orderDeliveryAddress = orderDeliveryAddress
        orderVO.orderCustomerPhoneNumber = orderCustomerPhoneNumber
        orderVO.orderPaymentOption = orderPaymentOption.num
        orderVO.orderDeliveryOption = orderDeliveryOption.num
        orderVO.orderDeliveryDueDate = orderDeliveryDueDate
        orderVO.orderIsOneTimeDelivery = orderIsOneTimeDelivery.bool
        orderVO.orderEtc = orderEtc
        orderVO.orderTimeStamp = orderTimeStamp
        orderVO.orderState = orderState.num

        return orderVO

    }

}