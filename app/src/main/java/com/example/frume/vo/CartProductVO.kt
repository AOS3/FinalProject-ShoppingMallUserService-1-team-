package com.example.frume.vo

import com.example.frume.model.CartProductModel
import com.example.frume.util.CartProductIsPurchasesBoolType
import com.example.frume.util.CartProductState
import com.example.frume.util.CartProductSubscribeState
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
    var cartItemDeliveryCycleWeek = 1 // 1주

    // 배송 반복 주기2
    var cartItemDeliveryCycleDay = 1 // 월요일

    // 등록시간
    var cartItemDeliveryTimeStamp = Timestamp.now()

    // 구매 여부( 구매 할 물건인가?)
    var cartItemIsPurchases = true // 구매할 품목임

    // 상태
    var cartProductState = 1 // 정상



    fun toCartProductModel(): CartProductModel {
        val cartProductModel = CartProductModel()
        cartProductModel.cartProductDocId = cartProductDocId
        cartProductModel.cartDocId = cartDocId
        cartProductModel.customerDocId = customerDocId
        cartProductModel.cartItemProductDocId = cartItemProductDocId
        cartProductModel.cartItemProductQuantity = cartItemProductQuantity
        cartProductModel.cartItemDeliveryTimeStamp = cartItemDeliveryTimeStamp
        cartProductModel.cartItemDeliveryDueDate = cartItemDeliveryDueDate


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

        when(cartItemIsPurchases){
            CartProductIsPurchasesBoolType.CART_PRODUCT_IS_PURCHASES_TRUE.bool->{cartProductModel.cartItemIsPurchases = CartProductIsPurchasesBoolType.CART_PRODUCT_IS_PURCHASES_TRUE}
            CartProductIsPurchasesBoolType.CART_PRODUCT_IS_PURCHASES_FALSE.bool->{cartProductModel.cartItemIsPurchases = CartProductIsPurchasesBoolType.CART_PRODUCT_IS_PURCHASES_FALSE}
            else->{}
        }

        when(cartProductState){
            CartProductState.CART_PRODUCT_STATE_NORMAL.num->{cartProductModel.cartProductState= CartProductState.CART_PRODUCT_STATE_NORMAL}
            CartProductState.CART_PRODUCT_STATE_ABNORMAL.num->{cartProductModel.cartProductState= CartProductState.CART_PRODUCT_STATE_ABNORMAL}
        }

        return cartProductModel
    }
}