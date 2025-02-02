package com.example.frume.repository

import android.util.Log
import com.example.frume.model.OrderModel
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

        // 🔹 사용자의 주문 목록을 Firestore에서 가져오는 메서드
        suspend fun gettingMyOrder(customerUserId: String): MutableList<OrderVO> {
            Log.d("test100", "OrderRepository->gettingMyOrder() 호출됨")
            Log.d("test100", "customerUserId: $customerUserId")

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("orderData")

            return try {
                // 🔹 Firestore에서 해당 사용자의 주문 데이터를 가져옴
                val result = collectionReference
                    .whereEqualTo("orderCustomerDocId", customerUserId)
                    .get()
                    .await()

                Log.d("test100", "Firestore 조회 완료, 문서 개수: ${result.size()}")

                // 🔹 가져온 데이터를 OrderVO 객체 리스트로 변환
                val userOrderVoList = result.toObjects(OrderVO::class.java)

                // 🔹 변환된 데이터 로그 출력
                userOrderVoList.forEach { orderVO ->
                    Log.d("test100", "변환된 OrderVO: $orderVO")
                }

                userOrderVoList
            } catch (e: Exception) {
                Log.e("test100", "Firestore 데이터 조회 중 오류 발생: ${e.message}")
                mutableListOf()
            }
        }

        // 해당 주문 가져오기
        suspend fun gettingOrderByOrderDocId(orderDocId: String): OrderVO {
            Log.d("test100", "OrderRepository->gettingOrderByOrderDocId() 호출됨")

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("orderData")

            return try {
                // orderDocId에 해당하는 문서를 가져옴
                val documentSnapshot = collectionReference.document(orderDocId).get().await()
                // 문서를 OrderVO 객체로 변환
                val orderVO = documentSnapshot.toObject(OrderVO::class.java)

                if (orderVO == null) {
                    Log.e("test100", "OrderRepository->gettingOrderByOrderDocId() : OrderVO 변환 실패, null 값 반환")
                    throw Exception("OrderVO 변환 실패 - orderDocId: $orderDocId")
                } else {
                    Log.d("test100", "OrderRepository->gettingOrderByOrderDocId() : 주문 데이터 가져오기 성공")
                }
                orderVO
            } catch (e: Exception) {
                Log.e("test100", "OrderRepository->gettingOrderByOrderDocId() 오류: ${e.message}")
                throw e  // 호출하는 곳에서 에러를 처리할 수 있도록 예외 재던짐
            }
        }


    }
}