package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.CartVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartRepository {
    companion object{

        // 내 장바구니 만들기 hj
        suspend fun addMyCart(cartVO: CartVO) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("cartData")
            val documentReference = collectionReference.document()
            val addCartVO = cartVO
            addCartVO.cartDocId = documentReference.id
            documentReference.set(addCartVO)
        }

        // 내 장바구니 가져오기 hj
        suspend fun gettingMyCart(customerUserId:String) : MutableList<CartVO> {

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("cartData")
            val result = collectionReference.whereEqualTo("customerDocId", customerUserId).get().await()
            val userCartVoList = result.toObjects(CartVO::class.java)

            return userCartVoList
        }

    }
}