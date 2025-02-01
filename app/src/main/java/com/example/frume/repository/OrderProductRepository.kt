package com.example.frume.repository

import android.util.Log
import com.example.frume.model.OrderProductModel
import com.example.frume.util.OrderSearchPeriod
import com.example.frume.util.OrderState
import com.example.frume.vo.OrderProductVO
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar

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

        suspend fun gettingMyOrderProductItems(
            ordersDocId: List<String>,
            orderSearchPeriod: OrderSearchPeriod
        ): MutableList<OrderProductVO> {
            val orderProductModelList = mutableListOf<OrderProductVO>()
            val firestore = FirebaseFirestore.getInstance()

            // 🔹 조회 기간 설정 (현재 시간 기준)
            val calendar = Calendar.getInstance().apply {
                when (orderSearchPeriod) {
                    OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL -> {} // 전체 조회 (필터 없음)
                    OrderSearchPeriod.ORDER_SEARCH_PERIOD_15DAYS -> add(Calendar.DAY_OF_YEAR, -15)
                    OrderSearchPeriod.ORDER_SEARCH_PERIOD_ONE_MONTH -> add(Calendar.MONTH, -1)
                    OrderSearchPeriod.ORDER_SEARCH_PERIOD_THREE_MONTH -> add(Calendar.MONTH, -3)
                    OrderSearchPeriod.ORDER_SEARCH_PERIOD_SIX_MONTH -> add(Calendar.MONTH, -6)
                }
            }
            val searchStartDate = Timestamp(calendar.time)

            try {
                // 🔹 orderData 컬렉션에서 날짜 필터링하여 해당 주문 ID 가져오기
                val orderQuerySnapshot = if (orderSearchPeriod == OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL) {
                    firestore.collection("orderData").get().await() // 전체 조회
                } else {
                    firestore.collection("orderData")
                        .whereGreaterThanOrEqualTo("orderTimeStamp", searchStartDate) // 🔥 날짜 필터 적용
                        .get()
                        .await()
                }

                for (orderDocument in orderQuerySnapshot.documents) {
                    val orderId = orderDocument.id
                    val getOrderState = orderDocument.get("orderState") ?: 0 // 🔥 orderState 값 가져오기 (기본값 0)

                    // 해당 주문의 상태를 주문상품의 상태에 집어넣기 위한 작업
                    val orderStateNum: Int = when (getOrderState) {
                        OrderState.ORDER_STATE_PAYMENT_PENDING.num -> OrderState.ORDER_STATE_PAYMENT_PENDING.num
                        OrderState.ORDER_STATE_PAYMENT_COMPLETED.num -> OrderState.ORDER_STATE_PAYMENT_COMPLETED.num
                        OrderState.ORDER_STATE_CANCELLED.num -> OrderState.ORDER_STATE_CANCELLED.num
                        OrderState.ORDER_STATE_RETURNED.num -> OrderState.ORDER_STATE_RETURNED.num
                        else -> OrderState.ORDER_STATE_EXCHANGED.num
                    }

                    val orderProductRef = firestore.collection("orderData")
                        .document(orderId)
                        .collection("orderProductItems")

                    val querySnapshot = orderProductRef.get().await()
                    for (document in querySnapshot.documents) {
                        val orderProduct = document.toObject(OrderProductVO::class.java)
                        orderProduct?.let {
                            /*it.orderState = orderStateNum // 🔥 orderState 값을 orderProduct에 설정
                            orderProductModelList.add()*/
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("FirebaseError", "Error fetching order products: ${e.message}")
            }

            return orderProductModelList
        }




        // 내 주문에서 상품 목록 1개 가져오기 hj
        suspend fun gettingMyOrderProductItem(orderDocId: String, orderProductDocId: String): OrderProductVO {
            val firestore = FirebaseFirestore.getInstance()

            // Firestore에서 선택된 장바구니 문서의 특정 항목 가져오기
            val documentSnapshot = firestore.collection("orderData")
                .document(orderDocId)
                .collection("orderProductItems")
                .document(orderProductDocId)
                .get()
                .await()

            // 문서가 존재하지 않으면 예외를 발생
            if (!documentSnapshot.exists()) {
                throw IllegalStateException("Document with ID $orderProductDocId does not exist in orderData/$orderDocId/cartProductItems")
            }

            // 문서를 CartProductVO로 변환
            val result = documentSnapshot.toObject(OrderProductVO::class.java)
                ?: throw IllegalStateException("Failed to convert document to OrderProductVO")

            return result
        }

    }
}