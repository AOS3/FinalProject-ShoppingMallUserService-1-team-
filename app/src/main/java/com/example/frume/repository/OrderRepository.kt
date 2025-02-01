package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.CartVO
import com.example.frume.vo.OrderVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepository {
    companion object{
        // 주문 생성 메서드
        suspend fun addMyOrder(orderVO: OrderVO): String {
            return try {
                val firestore = FirebaseFirestore.getInstance()
                val collectionReference = firestore.collection("orderData")
                val documentReference = collectionReference.document()

                // 새 문서 ID 생성
                orderVO.orderDocId = documentReference.id

                // Firestore에 데이터 저장
                documentReference.set(orderVO).await()  // await()로 비동기 처리 기다리기

                // 데이터 저장 성공 시 orderDocId 반환
                return orderVO.orderDocId
            } catch (e: Exception) {
                // 예외 발생 시 error 문자열 반환
                Log.e("addMyOrder", "에러 발생: ${e.message}", e)
                return "error"
            }
        }

        suspend fun gettingMyOrder(customerUserId: String) :MutableList<OrderVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("orderData")
            val result = collectionReference.whereEqualTo("customerDocId", customerUserId).get().await()
            val userOrderVoList = result.toObjects(OrderVO::class.java)

            return userOrderVoList

        }

    }
}