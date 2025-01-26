package com.example.frume.vo

import com.example.frume.model.CartProductModel
import com.example.frume.util.CartProductIsCheckStateBoolType
import com.example.frume.util.CartProductState
import com.example.frume.util.DeliveryCycleDays
import com.example.frume.util.DeliveryCycleWeeks
import com.example.frume.util.DeliverySubscribeState
import com.google.firebase.Timestamp

class CartProductVO {
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

    // 구독 여부
    var cartItemIsSubscribed = 0 // 0 : 비구독

    // 배송 예정일
    var cartItemDeliveryDueDate = Timestamp.now()

    // 배송 반복 주기1
    var cartItemDeliveryCycleWeek = 0 // 1회 구매

    // 배송 반복 주기2
    var cartItemDeliveryCycleDay = 0// 1회 구매

    // 등록시간
    var cartItemDeliveryTimeStamp = Timestamp.now()

    // 구매 여부( 구매 할 물건인가?)
    var cartItemIsCheckState = true // 구매할 품목임

    // 상태
    var cartProductState = 1 // 정상

    // 단가 * 수량 가격
    var cartProductPrice = 0

    // 이름
    var cartProductName = ""

    // 단가
    var cartProductUnitPrice = 0



    fun toCartProductModel(): CartProductModel {
        val cartProductModel = CartProductModel()
        cartProductModel.cartProductDocId = cartProductDocId
        cartProductModel.cartDocId = cartDocId
        cartProductModel.customerDocId = customerDocId
        cartProductModel.cartItemProductDocId = cartItemProductDocId
        cartProductModel.cartItemProductQuantity = cartItemProductQuantity
        cartProductModel.cartItemDeliveryTimeStamp = cartItemDeliveryTimeStamp
        cartProductModel.cartItemDeliveryDueDate = cartItemDeliveryDueDate
        cartProductModel.cartProductPrice = cartProductPrice
        cartProductModel.cartProductName = cartProductName
        cartProductModel.cartProductUnitPrice=cartProductUnitPrice


        when(cartItemIsSubscribed){
            DeliverySubscribeState.DELIVERY_STATE_NOT_SUBSCRIBE.num->{cartProductModel.cartItemIsSubscribed=DeliverySubscribeState.DELIVERY_STATE_NOT_SUBSCRIBE}
            DeliverySubscribeState.DELIVERY_STATE_SUBSCRIBE.num->{cartProductModel.cartItemIsSubscribed=DeliverySubscribeState.DELIVERY_STATE_SUBSCRIBE}
        }

        when(cartItemDeliveryCycleWeek){
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_ONE.num->{cartProductModel.cartItemDeliveryCycleWeek=DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_ONE}
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_TWO.num->{cartProductModel.cartItemDeliveryCycleWeek=DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_TWO}
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_THREE.num->{cartProductModel.cartItemDeliveryCycleWeek=DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_THREE}
            DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_FOUR.num->{cartProductModel.cartItemDeliveryCycleWeek=DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_FOUR}
        }

        when(cartItemDeliveryCycleDay){
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_MONDAY.num->{cartProductModel.cartItemDeliveryCycleDay=DeliveryCycleDays.DELIVERY_CYCLE_DAYS_MONDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_TUESDAY.num->{cartProductModel.cartItemDeliveryCycleDay=DeliveryCycleDays.DELIVERY_CYCLE_DAYS_TUESDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_WEDNESDAY.num->{cartProductModel.cartItemDeliveryCycleDay=DeliveryCycleDays.DELIVERY_CYCLE_DAYS_WEDNESDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_THURSDAY.num->{cartProductModel.cartItemDeliveryCycleDay=DeliveryCycleDays.DELIVERY_CYCLE_DAYS_THURSDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_FRIDAY.num->{cartProductModel.cartItemDeliveryCycleDay=DeliveryCycleDays.DELIVERY_CYCLE_DAYS_FRIDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SATURDAY.num->{cartProductModel.cartItemDeliveryCycleDay=DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SATURDAY}
            DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SUNDAY.num->{cartProductModel.cartItemDeliveryCycleDay=DeliveryCycleDays.DELIVERY_CYCLE_DAYS_SUNDAY}
        }

        when(cartItemIsCheckState){
            CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_TRUE.bool->{cartProductModel.cartItemIsCheckState = CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_TRUE}
            CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_FALSE.bool->{cartProductModel.cartItemIsCheckState = CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_FALSE}
            else->{}
        }

        when(cartProductState){
            CartProductState.CART_PRODUCT_STATE_NORMAL.num->{cartProductModel.cartProductState= CartProductState.CART_PRODUCT_STATE_NORMAL}
            CartProductState.CART_PRODUCT_STATE_ABNORMAL.num->{cartProductModel.cartProductState= CartProductState.CART_PRODUCT_STATE_ABNORMAL}
        }

        return cartProductModel
    }
}