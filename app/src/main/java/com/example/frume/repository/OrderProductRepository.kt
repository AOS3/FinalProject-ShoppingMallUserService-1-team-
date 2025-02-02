package com.example.frume.repository

import android.util.Log
import com.example.frume.model.OrderProductModel
import com.example.frume.util.OrderSearchPeriod
import com.example.frume.util.OrderState
import com.example.frume.vo.OrderProductVO
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
            ordersDocIdList: List<String>,
            orderSearchPeriod: OrderSearchPeriod
        ): MutableList<OrderProductVO> {
            val orderProductModelList = mutableListOf<OrderProductVO>()
            val firestore = FirebaseFirestore.getInstance()

            Log.d("test100", "OrderProductRepository->gettingMyOrderProductItems() 호출됨")
            Log.d("test100", "입력된 주문 ID 목록: $ordersDocIdList")
            Log.d("test100", "검색 기간: $orderSearchPeriod")

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
            Log.d("test100", "검색 시작 날짜 (Timestamp): $searchStartDate")

            try {
                val deferredList = ordersDocIdList.map { orderId ->
                    CoroutineScope(Dispatchers.IO).async {
                        Log.d("test100", "🔥 주문 ID 처리 중: $orderId")

                        val orderDocRef = firestore.collection("orderData").document(orderId)
                        val orderDoc = orderDocRef.get().await()

                        if (!orderDoc.exists()) {
                            Log.w("test100", "🚨 주문 문서 없음: $orderId")
                            return@async emptyList<OrderProductVO>()
                        }

                        val orderTimestamp = orderDoc.getTimestamp("orderTimeStamp")
                        if (orderTimestamp == null) {
                            Log.w("test100", "🚨 주문 타임스탬프 없음: $orderId")
                            return@async emptyList<OrderProductVO>()
                        }

                        Log.d("test100", "✔ 주문 타임스탬프: $orderTimestamp")

                        if (orderSearchPeriod != OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL &&
                            orderTimestamp < searchStartDate) {
                            Log.d("test100", "⏳ 주문 $orderId 검색 제외 (검색 기간 초과)")
                            return@async emptyList<OrderProductVO>()
                        }

                        // 🔥 orderState 가져오기
                        val getOrderState = orderDoc.get("orderState") ?: 0
                        val orderStateNum = when (getOrderState) {
                            OrderState.ORDER_STATE_PAYMENT_PENDING.num -> OrderState.ORDER_STATE_PAYMENT_PENDING.num
                            OrderState.ORDER_STATE_PAYMENT_COMPLETED.num -> OrderState.ORDER_STATE_PAYMENT_COMPLETED.num
                            OrderState.ORDER_STATE_CANCELLED.num -> OrderState.ORDER_STATE_CANCELLED.num
                            OrderState.ORDER_STATE_RETURNED.num -> OrderState.ORDER_STATE_RETURNED.num
                            else -> OrderState.ORDER_STATE_EXCHANGED.num
                        }

                        Log.d("test100", "✔ 주문 상태: $orderStateNum")

                        val orderProductRef = orderDocRef.collection("orderProductItems")
                        val querySnapshot = orderProductRef.get().await()

                        Log.d("test100", "📦 주문 상품 개수: ${querySnapshot.documents.size}")

                        querySnapshot.documents.mapNotNull { document ->
                            document.toObject(OrderProductVO::class.java)?.apply {
                                this.orderState = orderStateNum // 주문 상태 설정
                            }
                        }
                    }
                }

                // 모든 비동기 작업 완료 후 리스트 합치기
                val results = deferredList.awaitAll()
                orderProductModelList.addAll(results.flatten())

                Log.d("test100", "✅ 최종 주문 상품 개수: ${orderProductModelList.size}")
            } catch (e: Exception) {
                Log.e("test100", "🚨 Firebase 오류: ${e.message}")
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