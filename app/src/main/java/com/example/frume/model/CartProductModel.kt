package com.example.frume.model

import com.example.frume.util.CartProductIsPurchasesBoolType
import com.example.frume.util.CartProductState
import com.example.frume.util.CartProductSubscribeState
import com.example.frume.util.DeliveryCycleDays
import com.example.frume.util.DeliveryCycleWeeks
import com.example.frume.util.DeliverySubscribeState
import com.example.frume.vo.CartProductVO
import com.google.firebase.Timestamp

// 장바구니 상품 Model

class CartProductModel {
    // 장바구니 상품 문서 ID
    var cartProductDocId = ""

    // 장바구니 문서 ID
    var cartDocId = ""

    // 회원 문서 ID
    var customerDocId = ""

    // 상품 문서 ID
    var cartItemProductDocId = ""

    // 상품 수량
    var cartItemProductQuantity = 0

    // 구독 상태
    var cartItemIsSubscribed = DeliverySubscribeState.DELIVERY_STATE_SUBSCRIBE // 0 : 비구독

    // 배송 예정일
    var cartItemDeliveryDueDate = Timestamp.now()

    // 배송 반복 주기1
    var cartItemDeliveryCycleWeek = DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_NONE // 1회 구매 hj

    // 배송 반복 주기2
    var cartItemDeliveryCycleDay = DeliveryCycleDays.DELIVERY_CYCLE_DAYS_NONE // 1회 구매 hj

    // 등록시간
    var cartItemDeliveryTimeStamp = Timestamp.now()

    // 구매 여부( 구매 할 물건인가?)
    var cartItemIsPurchases =
        CartProductIsPurchasesBoolType.CART_PRODUCT_IS_PURCHASES_TRUE // 구매할 품목

    // 상태
    var cartProductState = CartProductState.CART_PRODUCT_STATE_NORMAL // 1 : 정상

    // 가격
    var cartProductPrice = 0

    // 이름
    var cartProductName = ""

    fun toCartProductVO(): CartProductVO {
        val cartProductVO = CartProductVO()
        cartProductVO.cartProductDocId = cartProductDocId
        cartProductVO.cartDocId = cartDocId
        cartProductVO.customerDocId = customerDocId
        cartProductVO.cartItemProductDocId = cartItemProductDocId
        cartProductVO.cartItemProductQuantity = cartItemProductQuantity
        cartProductVO.cartItemIsSubscribed = cartItemIsSubscribed.num
        cartProductVO.cartItemDeliveryDueDate = cartItemDeliveryDueDate
        cartProductVO.cartItemDeliveryCycleWeek = cartItemDeliveryCycleWeek.num
        cartProductVO.cartItemDeliveryCycleDay = cartItemDeliveryCycleDay.num
        cartProductVO.cartItemDeliveryTimeStamp = cartItemDeliveryTimeStamp
        cartProductVO.cartItemIsPurchases = cartItemIsPurchases.bool
        cartProductVO.cartProductState = cartProductState.num
        cartProductVO.cartProductPrice=cartProductPrice
        cartProductVO.cartProductName=cartProductName

        return cartProductVO
    }

}