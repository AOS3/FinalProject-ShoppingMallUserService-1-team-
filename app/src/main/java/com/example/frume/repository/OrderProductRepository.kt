package com.example.frume.repository

import android.util.Log
import com.example.frume.model.OrderProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderProductRepository {
    companion object{
        // 내 주문상품에 추가하기
        suspend fun addOrderProduct(orderDocId: String, orderProductModelList: MutableList<OrderProductModel>): Boolean {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("orderData")

            try {
                // cartDocId로 문서 직접 참조
                val orderDocument = collectionReference.document(orderDocId)

                // 문서 존재 여부 확인
                val orderDocumentSnapshot = orderDocument.get().await()

                if (orderDocumentSnapshot.exists()) {
                    // 서브컬렉션에 데이터 추가
                    val subCollectionRef = orderDocument.collection("orderProductItems")

                    // orderProductModelList의 각 항목에 대해 서브컬렉션에 문서 생성
                    orderProductModelList.forEach { orderProductModel ->
                        val selectedCartProductVO = orderProductModel.toOrderProductVO()

                        // 서브컬렉션의 문서 생성
                        val subDoc = subCollectionRef.document()
                        // 서브컬렉션의 문서 ID VO에 넣기
                        selectedCartProductVO.orderProductDocId = subDoc.id

                        // 문서 저장
                        subDoc.set(selectedCartProductVO).await()

                        Log.d("OrderProductRepository -> addOrderProduct()", "성공: $subDoc")
                    }
                    // 모든 작업이 성공적으로 끝난 경우 true 반환
                    return true
                } else {
                    Log.e("OrderProductRepository -> addOrderProduct", "orderDocId에 해당하는 orderProduct 문서를 찾을 수 없다: $orderDocId")
                    return false
                }
            } catch (e: Exception) {
                Log.e("addOrderProduct", "에러 발생", e)
                return false
            }
        }



    }
}