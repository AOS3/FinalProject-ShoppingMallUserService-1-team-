package com.example.frume.model

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

    // 배송 DocID
    var deliverDocId = ""

    // 결제 방식
    var orderPaymentOption = OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT // 1 : 게좌 이체

    // 주문 날짜
    var orderTimeStamp = Timestamp.now()

    // 주문 상태
    var orderState = OrderState.ORDER_STATE_PAYMENT_PENDING // 1 : 결제 대기중

    // 배송비
    var orderDeliveryCost = 0

    // 적립금 사용액
    var usedReward = 0


    fun toOrderVO(): OrderVO {

        val orderVO = OrderVO()
        orderVO.orderDocId = orderDocId
        orderVO.orderCustomerDocId = orderCustomerDocId
        orderVO.orderPaymentOption = orderPaymentOption.num
        orderVO.orderTimeStamp = orderTimeStamp
        orderVO.orderState = orderState.num
        orderVO.deliverDocId=deliverDocId
        orderVO.orderDeliveryCost = orderDeliveryCost
        orderVO.usedReward = usedReward

        return orderVO

    }

}