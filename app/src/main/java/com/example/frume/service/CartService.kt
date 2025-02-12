package com.example.frume.service

import android.util.Log
import com.example.frume.model.CartModel
import com.example.frume.repository.CartRepository

class CartService {
    companion object{

        // 내 장바구니 만들기 hj
        suspend fun addMyCart(cartModel: CartModel) {
            // 데이터를 VO에 담아준다.
            val cartVO = cartModel.toCartVO()
            // 저장하는 메서드를 호출한다.
            CartRepository.addMyCart(cartVO)
        }

        // 내 장바구니 가져오기 hj
        suspend fun gettingMyCart(customerUserDocId:String) : CartModel {
            val cartVoList = CartRepository.gettingMyCart(customerUserDocId)

            // 리스트가 비어 있는지 확인하고 처리
            if (cartVoList.isNotEmpty()) {
                return  cartVoList[0].toCartModel()  // 첫 번째 카트 모델 반환
            } else {
                Log.d("test100", "장바구니 데이터가 없습니다. 기본 값 반환.")
                return CartModel()  // 비어 있을 경우 기본 값을 반환 (필요 시 기본 값 설정 가능)
            }
        }
    }
}