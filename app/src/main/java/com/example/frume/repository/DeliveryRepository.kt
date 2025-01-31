package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.DeliveryVO
import com.google.firebase.firestore.FirebaseFirestore

class DeliveryRepository {
    companion object{
        // 배송 추가 메서드
        fun addUserDelivery(deliveryVO: DeliveryVO): Boolean {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryData")
            val documentReference = collectionReference.document()
            val addDeliveryVO = deliveryVO

            return try {
                // 새로운 배송 문서의 ID를 설정
                addDeliveryVO.deliveryDocId = documentReference.id

                // Firestore에 데이터 저장
                documentReference.set(addDeliveryVO)

                // 데이터 저장 성공 시 true 반환
                true
            } catch (e: Exception) {
                // 예외 발생 시 false 반환
                Log.e("addUserDelivery", "에러 발생: ${e.message}", e)
                false
            }
        }
    }
}