package com.example.frume.vo

import com.example.frume.model.OrderProductModel
import com.example.frume.util.OrderProductState
import com.example.frume.util.OrderState
import com.google.firebase.Timestamp

class OrderProductVO {
    // 주문 상품 ID
    var orderProductDocId = ""

    // 주문 ID
    var orderId = ""

    // 상품 문서 ID , 추가 hj
    var productDocId = ""

    // 상품 이름
    var orderProductName = ""

    // 상품 가격
    var orderProductPrice = 0

    // 상품 수량
    var orderProductCount = 0

    // 상품 이미지
    var orderProductImagePath = ""

    // 최종 가격
    var orderProductTotalPrice = 0

    // 상태
    var orderProductState = 1 // 1 : 정상

    // 등록시간
    var orderProductTimeStamp = Timestamp.now()

    // 주문 확정일
    var orderFixedDate:Timestamp? = null

    // 배송 예정일
    var orderDeliveryDueDate = Timestamp.now()

    // 주문 상태 (결제 대기, 완료, 취소, 반품, 교환)
    var orderState = OrderState.ORDER_STATE_PAYMENT_PENDING.num // 결제 대기

    fun toOrderProductModel(): OrderProductModel {
        val orderProductModel = OrderProductModel()

        orderProductModel.orderProductDocId = orderProductDocId
        orderProductModel.orderId = orderId
        orderProductModel.productDocId = productDocId
        orderProductModel.orderProductName = orderProductName
        orderProductModel.orderProductPrice = orderProductPrice
        orderProductModel.orderProductCount = orderProductCount
        orderProductModel.orderProductImagePath = orderProductImagePath
        orderProductModel.orderProductTotalPrice = orderProductTotalPrice
        orderProductModel.orderProductTimeStamp = orderProductTimeStamp
        orderProductModel.orderFixedDate = orderFixedDate
        orderProductModel.orderDeliveryDueDate = orderDeliveryDueDate

        when (orderProductState) {
            OrderProductState.ORDER_PRODUCT_STATE_NORMAL.num->{OrderProductState.ORDER_PRODUCT_STATE_NORMAL}
            OrderProductState.ORDER_PRODUCT_STATE_ABNORMAL.num->{OrderProductState.ORDER_PRODUCT_STATE_ABNORMAL}
        }

        when(orderState){
            OrderState.ORDER_STATE_PAYMENT_PENDING.num->{OrderState.ORDER_STATE_PAYMENT_PENDING}
            OrderState.ORDER_STATE_PAYMENT_COMPLETED.num->{OrderState.ORDER_STATE_PAYMENT_COMPLETED}
            OrderState.ORDER_STATE_CANCELLED.num->{OrderState.ORDER_STATE_CANCELLED}
            OrderState.ORDER_STATE_RETURNED.num->{OrderState.ORDER_STATE_RETURNED}
            OrderState.ORDER_STATE_EXCHANGED.num->{OrderState.ORDER_STATE_EXCHANGED}
        }


        return orderProductModel
    }



}