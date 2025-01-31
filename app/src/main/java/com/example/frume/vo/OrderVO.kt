package com.example.frume.vo

import com.example.frume.model.CartProductModel
import com.example.frume.model.OrderModel
import com.example.frume.util.OrderPaymentOption
import com.example.frume.util.OrderState
import com.google.firebase.Timestamp

class OrderVO {
    // 주문 문서 ID
    var orderDocId = ""

    // 주문 회원 문서 ID
    var orderCustomerDocId = ""

    // 배송 DocID
    var deliverDocId = ""

    // 결제 방식
    var orderPaymentOption = 1 // 1 : 게좌 이체

    // 주문 날짜
    var orderTimeStamp = Timestamp.now()

    // 주문 상태
    var orderState = 1 // 1 : 결제 대기중

    // 배송비
    var orderDeliveryCost = 0

    // 적립금 사용액
    var usedReward = 0


    fun toOrderModel(): OrderModel {

        val orderModel = OrderModel()
        orderModel.orderDocId = orderDocId
        orderModel.orderCustomerDocId = orderCustomerDocId
        orderModel.orderTimeStamp = orderTimeStamp
        orderModel.deliverDocId=deliverDocId
        orderModel.orderDeliveryCost = orderDeliveryCost
        orderModel.usedReward

        when(orderPaymentOption){
            OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT}
            OrderPaymentOption.ORDER_PAYMENT_OPTION_CARD.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_CARD}
            OrderPaymentOption.ORDER_PAYMENT_OPTION_KAKAO_PAY.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_KAKAO_PAY}
            OrderPaymentOption.ORDER_PAYMENT_OPTION_NAVER_PAY.num->{orderModel.orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_NAVER_PAY}

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