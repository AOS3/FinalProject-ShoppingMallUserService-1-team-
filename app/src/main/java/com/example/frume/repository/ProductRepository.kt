package com.example.frume.repository

import android.util.Log
import com.example.frume.model.ProductModel
import com.example.frume.vo.ProductVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository {
    companion object {
        // 카테고리별 목록 가져오기
        suspend fun gettingProductByCategory(productCategoryType: String): MutableList<ProductVO> {
            Log.d("test100", "ProductRepository : gettingProductByCategory")
            val firestore = FirebaseFirestore.getInstance()

            val collectionReference = firestore.collection("productData")
            Log.d("test100", "ProductRepository -> collectionReference: ${collectionReference.id}")

            val result = mutableListOf<ProductVO>()

            try {
                val querySnapshot = when (productCategoryType) {
                    "국산" -> {
                        Log.d("test100", "ProductRepository -> collectionReference-> category1 : 국산")
                        collectionReference.whereEqualTo("productCategory1", "국산").get().await()
                    }

                    "수입" -> {
                        Log.d("test100", "ProductRepository -> collectionReference-> category1 : 수입")
                        collectionReference.whereEqualTo("productCategory1", "수입").get().await()
                    }

                    else -> {
                        Log.d("test100", "ProductRepository -> collectionReference-> category1 : 그 외")
                        collectionReference.whereEqualTo("productCategory2", productCategoryType).get()
                            .await()
                    }

                }
                Log.d("test100", "ProductRepository -> collectionReference-> querySnapshot : ${querySnapshot.documents}")


                for (document in querySnapshot) {
                    val productVO = document.toObject(ProductVO::class.java)
                    Log.d("test100", "ProductRepository -> productVO: ${productVO}")

                    result.add(productVO)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }

        suspend fun getProductInfo(productID: String): MutableList<ProductVO> {
            val firestore = FirebaseFirestore.getInstance()

            val collectionReference = firestore.collection("productData")
            val productResult = mutableListOf<ProductVO>()

            try {
                val product = collectionReference.whereEqualTo("productDocId", productID).get().await()

                for (document in product) {
                    val productVO = document.toObject(ProductVO::class.java)
                    Log.d("test100", "ProductRepository -> productVO: $productVO")

                    productResult.add(productVO)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return productResult
        }
    }
}