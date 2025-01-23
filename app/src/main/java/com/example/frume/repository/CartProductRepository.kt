package com.example.frume.repository

import android.util.Log
import com.example.frume.model.CartProductModel
import com.example.frume.model.ProductModel
import com.example.frume.vo.CartProductVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartProductRepository {
    companion object{
        // 내장바구니에 상품 전체 목록 가져오기 hj
        suspend fun gettingMyCartProductItems(cartDocId: String) : MutableList<CartProductVO> {
            val result = mutableListOf<CartProductVO>()
            val firestore = FirebaseFirestore.getInstance()

            // 선택된 장바구니 문서에서 cartProductItems 서브컬렉션을 가져오기
            val cartProductItems = firestore.collection("cartData")
                .document(cartDocId)
                .collection("cartProductItems")
                .get()
                .await()

            for (product in cartProductItems) {
                val cartProduct = product.toObject(CartProductVO::class.java)
                result.add(cartProduct)
            }
            Log.d("test100","CartProductRepository->gettingMyCartProductItems : ${result}")

            return result
        }


        // 내 장바구니 상품목록에 추가하기, 장바구니 서브컬렉션에 추가, 장바구니 부를때 항상 장바구니 상품들을 부르기 때문 hj
        suspend fun addMyCartProduct(cartDocId: String, cartProductModel: CartProductModel) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("cartData")
            val selectedProductVO = cartProductModel.toCartProductVO()

            try {
                // cartDocId로 문서 검색
                val selectedCartDocs = collectionReference.whereEqualTo("cartDocId", cartDocId).get().await()

                if (selectedCartDocs.documents.isNotEmpty()) {
                    // 검색된 첫 번째 문서를 참조
                    val cartDocument = selectedCartDocs.documents.first().reference

                    // 서브컬렉션에 데이터 추가
                    val subCollectionRef = cartDocument.collection("cartProductItems")
                    subCollectionRef.add(selectedProductVO).await()

                    Log.d("addMyCartProduct", "성공: $selectedProductVO")
                } else {
                    Log.e("addMyCartProduct", "cartDocId에 해당하는 장바구니 문서를 찾을 수 없다: $cartDocId")
                }
            } catch (e: Exception) {
                Log.e("addMyCartProduct", "에러 발생", e)
            }
        }

    }
}