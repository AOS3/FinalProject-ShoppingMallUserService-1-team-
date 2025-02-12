package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.DeliveryVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DeliveryRepository {
    companion object{
        // 배송 추가 메서드
        fun addUserDelivery(deliveryVO: DeliveryVO): String {
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
                documentReference.id
            } catch (e: Exception) {
                // 예외 발생 시 false 반환
                Log.e("addUserDelivery", "에러 발생: ${e.message}", e)
                "false"
            }
        }

        // 해당 주문 가져오기
        suspend fun gettingDeliveryByDocId(deliveryDocId: String): DeliveryVO {
            Log.d("test100", "DeliveryRepository->gettingDeliveryByDocId() 호출됨")

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryData")

            return try {
                // orderDocId에 해당하는 문서를 가져옴
                val documentSnapshot = collectionReference.document(deliveryDocId).get().await()
                // 문서를 OrderVO 객체로 변환
                val deliveryVO = documentSnapshot.toObject(DeliveryVO::class.java)

                if (deliveryVO == null) {
                    Log.e("test100", "DeliveryRepository->gettingDeliveryByDocId() : DeliveryVO 변환 실패, null 값 반환")
                    throw Exception("OrderVO 변환 실패 - orderDocId: $deliveryDocId")
                } else {
                    Log.d("test100", "DeliveryRepository->gettingDeliveryByDocId() : 주문 데이터 가져오기 성공")
                }
                deliveryVO
            } catch (e: Exception) {
                Log.e("test100", "DeliveryRepository->gettingDeliveryByDocId() 오류: ${e.message}")
                throw e  // 호출하는 곳에서 에러를 처리할 수 있도록 예외 재던짐
            }
        }
    }
}