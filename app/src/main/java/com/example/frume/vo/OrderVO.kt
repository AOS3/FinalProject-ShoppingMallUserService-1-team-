package com.example.frume.vo

import com.example.frume.model.CartProductModel
import com.example.frume.model.OrderModel
import com.example.frume.util.OrderDeliveryOption
import com.example.frume.util.OrderIsOneTimeDeliveryBoolType
import com.example.frume.util.OrderPaymentOption
import com.example.frume.util.OrderState
import com.google.firebase.Timestamp

class OrderVO {
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
    var orderPaymentOption = 1 // 1 : 게좌 이체

    // 배송 방식
    var orderDeliveryOption = 1 // 1: 문 앞 배송

    // 배송 예정일
    var orderDeliveryDueDate = Timestamp.now()

    // 일회성 배송 여부
    var orderIsOneTimeDelivery = true // true : 일회성배송

    // 기타사항
    var orderEtc = ""

    // 주문 날짜
    var orderTimeStamp = Timestamp.now()

    // 주문 상태
    var orderState = 1 // 1 : 결제 대기중


    fun toOrderModel(): OrderModel {

        val orderModel = OrderModel()
        orderModel.orderDocId = orderDocId
        orderModel.orderCustomerDocId = orderCustomerDocId
        orderModel.cartProducts = cartProducts
        orderModel.orderDeliveryAddress = orderDeliveryAddress
        orderModel.orderCustomerPhoneNumber = orderCustomerPhoneNumber
        orderModel.orderDeliveryDueDate = orderDeliveryDueDate
        orderModel.orderEtc = orderEtc
        orderModel.orderTimeStamp = orderTimeStamp

        when(orderPaymentOption){
            OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT}
            OrderPaymentOption.ORDER_PAYMENT_OPTION_CARD.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_CARD}
            OrderPaymentOption.ORDER_PAYMENT_OPTION_KAKAO_PAY.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_KAKAO_PAY}
            OrderPaymentOption.ORDER_PAYMENT_OPTION_NAVER_PAY.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_NAVER_PAY}

        }

        when(orderDeliveryOption){
            OrderDeliveryOption.DOOR_DELIVERY.num->{orderModel.orderDeliveryOption = OrderDeliveryOption.DOOR_DELIVERY}
            OrderDeliveryOption.PARCEL_LOCKER.num->{orderModel.orderDeliveryOption = OrderDeliveryOption.PARCEL_LOCKER}
            OrderDeliveryOption.SECURITY_OFFICE.num->{orderModel.orderDeliveryOption = OrderDeliveryOption.SECURITY_OFFICE}

        }

        when(orderIsOneTimeDelivery){
            OrderIsOneTimeDeliveryBoolType.ONE_TIME_DELIVERY.bool->{orderModel.orderIsOneTimeDelivery= OrderIsOneTimeDeliveryBoolType.ONE_TIME_DELIVERY}
            OrderIsOneTimeDeliveryBoolType.REGULAR_DELIVERY.bool->{orderModel.orderIsOneTimeDelivery= OrderIsOneTimeDeliveryBoolType.REGULAR_DELIVERY}
            else -> {}
        }

        when(orderState){
            OrderState.ORDER_STATE_PAYMENT_PENDING.num->{OrderState.ORDER_STATE_PAYMENT_PENDING}
            OrderState.ORDER_STATE_PAYMENT_COMPLETED.num->{OrderState.ORDER_STATE_PAYMENT_COMPLETED}
            OrderState.ORDER_STATE_CANCELLED.num->{OrderState.ORDER_STATE_CANCELLED}
            OrderState.ORDER_STATE_RETURNED.num->{OrderState.ORDER_STATE_RETURNED}
            OrderState.ORDER_STATE_EXCHANGED.num->{OrderState.ORDER_STATE_EXCHANGED}

        }

        return orderModel
    }
}