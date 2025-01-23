package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.ProductVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.coroutines.tasks.await

class ProductRepository {
    companion object {
        // 카테고리별 목록 가져오기
        suspend fun gettingProductByCategory(productCategoryType: String): MutableList<ProductVO> {

            // Log.d("test100","ProductRepository : gettingProductByCategory")
            val firestore = FirebaseFirestore.getInstance()

            val collectionReference = firestore.collection("productData")

            val result = mutableListOf<ProductVO>()

            try {
                val querySnapshot = when (productCategoryType) {
                    "국산" -> {
                        Log.d("test100","ProductRepository -> collectionReference-> category1 : 국산")
                        collectionReference.whereEqualTo("productCategory1", "국산").get().await()
                    }
                    "수입" -> {
                        Log.d("test100","ProductRepository -> collectionReference-> category1 : 수입")
                        collectionReference.whereEqualTo("productCategory1", "수입").get().await()
                    }
                    else -> {
                        Log.d("test100","ProductRepository -> collectionReference-> category1 : 그 외")
                        collectionReference.whereEqualTo("productCategory2", productCategoryType).get()
                            .await()
                    }

                }

                for (document in querySnapshot) {
                    val productVO = document.toObject(ProductVO::class.java)

                    result.add(productVO)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        // 홈화면 recyclerview 전체 가져오기
        // 카테고리별 목록 가져오기
        suspend fun gettingProductAll(): MutableList<ProductVO> {
            Log.d("test100","ProductRepository : gettingProductAll()")
            val firestore = FirebaseFirestore.getInstance()

            val collectionReference = firestore.collection("productData")
            Log.d("test100","ProductRepository.gettingProductAll() -> collectionReference: ${collectionReference.id}")

            val result = collectionReference.orderBy("productTimeStamp", Query.Direction.DESCENDING).get()
                .await()

            Log.d("test100","ProductRepository.gettingProductAll > collectionReference-> querySnapshot : ${result.documents}\n")

            // 반환할 리스트
            val resultList = mutableListOf <ProductVO>()

            for (document in result) {
                val productVO = document.toObject(ProductVO::class.java)
                Log.d("test100","ProductRepository.gettingProductAll -> productVO: ${productVO}")

                resultList.add(productVO)
            }

            return resultList
        }
            // 홈화면 탭바 별로 가져오기(신제품, 특가, 베스트, 1인, 패키지)
    }
}
