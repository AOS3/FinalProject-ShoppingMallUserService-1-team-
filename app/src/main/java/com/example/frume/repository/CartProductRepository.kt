package com.example.frume.repository

import android.util.Log
import com.example.frume.model.CartProductModel
import com.example.frume.vo.CartProductVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartProductRepository {
    companion object{
        // 내장바구니에 상품 전체 목록 가져오기 hj
        suspend fun gettingMyCartProductItems(cartDocId: String): MutableList<CartProductVO> {
            val result = mutableListOf<CartProductVO>()
            val firestore = FirebaseFirestore.getInstance()

            try {
                // 선택된 장바구니 문서에서 cartProductItems 서브컬렉션을 가져오기
                val cartProductItems = firestore.collection("cartData")
                    .document(cartDocId)
                    .collection("cartProductItems")
                    .get()
                    .await()

                // 문서가 존재하지 않는 경우는 빈 결과로 처리
                if (!cartProductItems.isEmpty) {
                    for (product in cartProductItems) {
                        val cartProduct = product.toObject(CartProductVO::class.java)
                        result.add(cartProduct)
                    }
                }
            } catch (e: Exception) {
                // 예외 발생 시 처리 (로그를 남기거나 빈 목록을 반환)
                Log.e("CartError", "Error fetching cart product items: ${e.message}")
            }
            return result
        }

        // 내장바구니에 상품 목록 1개 가져오기 hj
        suspend fun gettingMyCartProductItem(cartDocId: String, cartProductDocId: String): CartProductVO {
            val firestore = FirebaseFirestore.getInstance()

            // Firestore에서 선택된 장바구니 문서의 특정 항목 가져오기
            val documentSnapshot = firestore.collection("cartData")
                .document(cartDocId)
                .collection("cartProductItems")
                .document(cartProductDocId)
                .get()
                .await()

            // 문서가 존재하지 않으면 예외를 발생
            if (!documentSnapshot.exists()) {
                throw IllegalStateException("Document with ID $cartProductDocId does not exist in cartData/$cartDocId/cartProductItems")
            }

            // 문서를 CartProductVO로 변환
            val result = documentSnapshot.toObject(CartProductVO::class.java)
                ?: throw IllegalStateException("Failed to convert document to CartProductVO")

            return result
        }



        // 내 장바구니 상품목록에 추가하기, 장바구니 서브컬렉션에 추가, 장바구니 부를때 항상 장바구니 상품들을 부르기 때문 hj
        suspend fun addMyCartProduct(cartDocId: String, cartProductModel: CartProductModel) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("cartData")
            val selectedCartProductVO = cartProductModel.toCartProductVO()

            try {
                // cartDocId로 문서 직접 참조
                val cartDocument = collectionReference.document(cartDocId)

                // 문서 존재 여부 확인
                val cartDocumentSnapshot = cartDocument.get().await()

                if (cartDocumentSnapshot.exists()) {
                    // 서브컬렉션에 데이터 추가
                    val subCollectionRef = cartDocument.collection("cartProductItems")
                    // 서브컬렉션의 문서 생성
                    val subDoc = subCollectionRef.document()
                    // 서브컬렉션의 문서 ID VO에 넣기
                    selectedCartProductVO.cartProductDocId = subDoc.id
                    subDoc.set(selectedCartProductVO).await()

                    Log.d("CartProductRepository -> addMyCartProduct()", "성공: $subDoc")
                } else {
                    Log.e("CartProductRepository -> addMyCartProduct", "cartDocId에 해당하는 장바구니 문서를 찾을 수 없다: $cartDocId")
                }
            } catch (e: Exception) {
                Log.e("addMyCartProduct", "에러 발생", e)
            }
        }



        // 내 장바구니 옵션 변경하기
        suspend fun changeCartProductOption(cartDocId: String, cartProductDocId: String, cartProductVO: CartProductVO) {

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("cartData")
            try {
                // cartDocId로 문서 직접 참조
                val cartDocument = collectionReference.document(cartDocId)

                // 문서 존재 여부 확인
                val cartDocumentSnapshot = cartDocument.get().await()

                if (cartDocumentSnapshot.exists()) {
                    // 서브컬렉션 내의 cartProductDocId 문서 참조
                    val subCollectionRef = cartDocument.collection("cartProductItems")
                    val subDoc = subCollectionRef.document(cartProductDocId)

                    // 해당 문서에 업데이트된 VO 저장
                    subDoc.set(cartProductVO).await()

                    Log.d("CartProductRepository -> changeCartProductOption()", "성공: $subDoc")
                } else {
                    Log.e("CartProductRepository -> changeCartProductOption", "cartDocId에 해당하는 장바구니 문서를 찾을 수 없습니다: $cartDocId")
                }
            } catch (e: Exception) {
                Log.e("CartProductRepository -> changeCartProductOption", "옵션 변경 중 에러 발생", e)
            }
        }

        // 장바구니에서 품목 제거
        suspend fun deleteCartProducts(cartDocId: String, selectedListDocId: MutableList<String>): Boolean {
            Log.d("test100","CartProductRepository ->deleteCartProducts()")

            val firestore = FirebaseFirestore.getInstance()

            // Firestore의 장바구니 데이터 접근
            val cartProductItemsRef = firestore.collection("cartData")
                .document(cartDocId)
                .collection("cartProductItems")

            // 각 문서를 삭제
            try {
                selectedListDocId.forEach { cartProductDocId ->
                    // 문서 삭제
                    cartProductItemsRef.document(cartProductDocId).delete().await()
                    Log.d("deleteCartProducts", "삭제 성공 ID $cartProductDocId")
                }
                return true // 모든 삭제 성공 시 true 반환
            } catch (e: Exception) {
                Log.e("deleteCartProducts", "삭제 중 오류 발생", e)
                return false // 삭제 중 오류 발생 시 false 반환
            }
        }
    }
}