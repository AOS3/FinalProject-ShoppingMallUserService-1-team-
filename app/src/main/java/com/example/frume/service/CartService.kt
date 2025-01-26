package com.example.frume.service

import android.util.Log
import com.example.frume.model.CartModel
import com.example.frume.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            val myCartModel = cartVoList[0].toCartModel()
            return myCartModel
        }



    }
}