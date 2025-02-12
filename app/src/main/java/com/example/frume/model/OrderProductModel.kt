package com.example.frume.model

import com.example.frume.util.DeliverySubscribeState
import com.example.frume.util.OrderProductState
import com.example.frume.util.OrderState
import com.example.frume.vo.OrderProductVO
import com.google.firebase.Timestamp

// 주문 상품 Model
// 주문결제 하고 난 상품들 목록
class OrderProductModel {
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
    var orderProductState = OrderProductState.ORDER_PRODUCT_STATE_NORMAL // 1 : 정상

    // 등록시간
    var orderProductTimeStamp = Timestamp.now()

    // 주문 확정일
    var orderFixedDate :Timestamp? = null

    // 배송 예정일
    var orderDeliveryDueDate = Timestamp.now()

    // 주문 상태 (결제 대기, 완료, 취소, 반품, 교환)
    var orderState = OrderState.ORDER_STATE_PAYMENT_PENDING // 결제 대기

    // 정기배송여부
    var deliveryIsSubscribed = DeliverySubscribeState.DELIVERY_STATE_NOT_SUBSCRIBE // 0 : 비구독

    fun toOrderProductVO(): OrderProductVO {
        val orderProductVO = OrderProductVO()

        orderProductVO.orderProductDocId = orderProductDocId
        orderProductVO.orderId = orderId
        orderProductVO.productDocId = productDocId
        orderProductVO.orderProductName = orderProductName
        orderProductVO.orderProductPrice = orderProductPrice
        orderProductVO.orderProductCount = orderProductCount
        orderProductVO.orderProductImagePath = orderProductImagePath
        orderProductVO.orderProductTotalPrice = orderProductTotalPrice
        orderProductVO.orderProductState = orderProductState.num
        orderProductVO.orderProductTimeStamp = orderProductTimeStamp
        orderProductVO.orderFixedDate = orderFixedDate
        orderProductVO.orderDeliveryDueDate = orderDeliveryDueDate
        orderProductVO.deliveryIsSubscribed = deliveryIsSubscribed.num



        return orderProductVO
    }
}
