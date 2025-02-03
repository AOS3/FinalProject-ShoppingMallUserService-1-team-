package com.example.frume.repository

import android.util.Log
import com.example.frume.data.Product
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
                        // Log.d("test100","ProductRepository -> collectionReference-> category1 : 국산")
                        collectionReference.whereEqualTo("productCategory1", "국산").get().await()
                    }

                    "수입" -> {
                        // Log.d("test100","ProductRepository -> collectionReference-> category1 : 수입")
                        collectionReference.whereEqualTo("productCategory1", "수입").get().await()
                    }

                    "1인 가구" -> {
                        collectionReference.whereEqualTo("productCategory3", "1인 가구").get().await()
                    }

                    "대용량" -> {
                        collectionReference.whereEqualTo("productCategory3", "대용량").get().await()
                    }

                    "패키지" -> {
                        collectionReference.whereEqualTo("productCategory3", "패키지").get().await()
                    }

                    "특가" -> {
                        collectionReference.whereEqualTo("productCategory3", "특가").get().await()
                    }

                    else -> {
                        // Log.d("test100","ProductRepository -> collectionReference-> category1 : 그 외")
                        collectionReference.whereEqualTo("productCategory2", productCategoryType).get()
                            .await()
                    }

                }
                // Log.d("test100","ProductRepository -> collectionReference-> querySnapshot : ${querySnapshot.documents}")


                for (document in querySnapshot) {
                    val productVO = document.toObject(ProductVO::class.java)
                    // Log.d("test100","ProductRepository -> productVO: ${productVO}")

                    result.add(productVO)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }


        // productDocId로 상품 정보 가져오기
        suspend fun getProductInfo(productID: String): Product {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")

            val product = collectionReference.whereEqualTo("productDocId", productID).get().await()

            return product.documents[0].toObject(Product::class.java)!!
        }

        // 홈화면 탭바 별로 가져오기(신제품, 특가, 베스트, 1인, 패키지)
// 상품 문서 ID로 상품 한 개 Model 가져오기
        suspend fun gettingProductOneByDocId(selectProductDocId: String): ProductVO {
            Log.d("ProductRepository", "gettingProductOneByDocId() 호출됨, selectProductDocId: $selectProductDocId")

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")

            return try {
                val querySnapshot = collectionReference.whereEqualTo("productDocId", selectProductDocId).get().await()
                val selectedProductVOList = querySnapshot.toObjects(ProductVO::class.java)

                Log.d("ProductRepository", "Firestore 결과 개수: ${selectedProductVOList.size}")

                if (selectedProductVOList.isNotEmpty()) {
                    Log.d("ProductRepository", "가져온 상품 정보: ${selectedProductVOList[0]}")
                    selectedProductVOList[0] // 첫 번째 항목 반환
                } else {
                    Log.d("ProductRepository", "해당 productDocId에 대한 상품이 없음")
                    ProductVO()
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "Firestore에서 상품 조회 중 오류 발생: ${e.message}", e)
                ProductVO()
            }
        }

        // productDocId로 상품 정보 가져오기
        suspend fun getSearchProductInfo(searchKeyword: String): MutableList<ProductVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")

            val queries = listOf(
                collectionReference.whereGreaterThanOrEqualTo("productName", searchKeyword)
                    .whereLessThan("productName", searchKeyword + "\uf8ff"),
                collectionReference.whereGreaterThanOrEqualTo("productCategory1", searchKeyword)
                    .whereLessThan("productCategory1", searchKeyword + "\uf8ff"),
                collectionReference.whereGreaterThanOrEqualTo("productCategory2", searchKeyword)
                    .whereLessThan("productCategory2", searchKeyword + "\uf8ff"),
                collectionReference.whereGreaterThanOrEqualTo("productCategory3", searchKeyword)
                    .whereLessThan("productCategory3", searchKeyword + "\uf8ff")
            )

            val resultSet = mutableSetOf<ProductVO>() // 중복 방지를 위한 Set 사용
            val seenProductIds = mutableSetOf<String>()  // 중복 확인용 productDocId Set

            for (query in queries) {
                val querySnapshot = query.get().await()
                for (product in querySnapshot.toObjects(ProductVO::class.java)) {
                    if (product.productDocId !in seenProductIds) {
                        seenProductIds.add(product.productDocId) // ID 등록
                        resultSet.add(product) // 중복 제거된 상품 추가
                    }
                }
            }
            return resultSet.toMutableList() // Set을 List로 변환하여 반환
        }
    }
    // 홈화면 recyclerview 전체 가져오기
    // 카테고리별 목록 가져오기
    suspend fun gettingProductAll(): List<Product> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("productData")
        val resultList = mutableListOf<Product>()

        try {
            val result = collectionReference.orderBy("productTimeStamp", Query.Direction.DESCENDING).get()
                .await()

            // 반환할 리스트
            for (document in result) {
                val productList = document.toObject(Product::class.java)
                resultList.add(productList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultList
    }

    suspend fun getBestProductCategory(): List<Product> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("productData")

        val resultList = mutableListOf<Product>()
        try {
            val result = collectionReference
                .orderBy("productSalesCount", Query.Direction.DESCENDING) // productSalesCount 기준 내림차순 정렬
                .get()
                .await()

            for (document in result) {
                val productList = document.toObject(Product::class.java)
                resultList.add(productList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultList
    }

    // 홈화면 recyclerview 전체 가져오기
    // 카테고리별 목록 가져오기
    suspend fun gettingProductCategory(category: String): List<Product> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("productData")

        val resultList = mutableListOf<Product>()

        try {
            val result = collectionReference
                .whereEqualTo("productCategory3", category)
                .get()
                .await()

            // 반환할 리스트
            for (document in result) {
                val productList = document.toObject(Product::class.java)
                resultList.add(productList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultList
    }

    // productDocId로 상품 정보 가져오기
    suspend fun getProductInfo(productID: String): Product {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("productData")

        val product = collectionReference.whereEqualTo("productDocId", productID).get().await()


        return product.documents[0].toObject(Product::class.java)!!
    }
}
