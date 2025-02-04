package com.example.frume.repository

import android.net.Uri
import android.util.Log
import com.example.frume.data.MyReviewParent
import com.example.frume.vo.ReviewVO
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date

class ReviewRepository {
    companion object {
        // 유저 후기 저장하기
        suspend fun setUserReview(reviewVO: ReviewVO): String {
            val fireStore = FirebaseFirestore.getInstance()
            val collectionReference = fireStore.collection("reviewData")
            val documentReference = collectionReference.document()
            reviewVO.reviewDocId = documentReference.id
            documentReference.set(reviewVO)
            return reviewVO.reviewDocId


        }

        // 이미지 저장하기
        suspend fun setUserReviewImg(uris: MutableList<Uri?>): MutableList<String> {
            val storage = Firebase.storage
            val storageRef = storage.getReference("review_img")
            val uploadedUrls = mutableListOf<String>()
            for (uri in uris) {
                val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                val imageRef = storageRef.child("${fileName}.png")

                try {
                    if (uri != null) {
                        imageRef.putFile(uri).await()
                    }
                    val downloadUrl = imageRef.downloadUrl.await()
                    uploadedUrls.add(downloadUrl.toString())
                } catch (_: Exception) {

                }
            }
            return uploadedUrls
        }

        suspend fun getMyReviewCount(userDocId: String): Int {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("reviewData")

            return try {
                val reviewSnapshot = collectionReference
                    .whereEqualTo("reviewCustomerDocId", userDocId) // 특정 유저의 리뷰만 필터링
                    .orderBy("reviewTimeStamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

                // 문서 개수를 로그로 출력
                Log.d("Firestore", "문서 개수: ${reviewSnapshot.documents.size}")
                reviewSnapshot.documents.size // 문서 개수 반환
            } catch (e: Exception) {
                e.printStackTrace()
                0 // 오류 발생 시 0 반환
            }
        }



        // 내가 작성한 특정 상품에 대한 후기 수 가져오기
        suspend fun getMyReviewCountByProduct(productDocId: String, userId: String): Int {

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("reviewData")

    }

    // sehoon userDocID로 리뷰 정보 가져오기
    suspend fun getAllReview(): List<MyReviewParent> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("reviewData")
        val reviewResult = mutableListOf<MyReviewParent>()


            return try {
                val reviewSnapshot = collectionReference
                    .whereEqualTo("reviewProductDocId", productDocId)
                    .whereEqualTo("reviewCustomerDocId", userId)
                    .get()
                    .await()

                Log.d("test333", "사용자($userId)가 작성한 후기 수: ${reviewSnapshot.documents.size}")

                reviewSnapshot.documents.size // 후기 수 반환

            } catch (e: Exception) {
                e.printStackTrace()
                0 // 오류 시 0 반환
            }
        }

        // 이 상품을 내가 주문한 횟수 가져오기
        suspend fun getMyOrderCountByProduct(productDocId: String, userId: String): Int {

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("orderData")
            var orderCount = 0

            try {
                // 사용자의 모든 주문 가져오기
                val orderSnapshot = collectionReference
                    .whereEqualTo("orderCustomerDocId", userId)  // 사용자 필터링
                    .get()
                    .await()

                for (orderDocument in orderSnapshot.documents) {
                    // 각 주문의 orderProductItems 컬렉션 확인
                    val orderProductItemsRef = orderDocument.reference.collection("orderProductItems")
                    val productSnapshot = orderProductItemsRef
                        .whereEqualTo("productDocId", productDocId)  // 특정 상품만 필터링
                        .get()
                        .await()

                    // 해당 주문에 포함된 특정 상품의 개수를 합산
                    orderCount += productSnapshot.documents.size
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Log.d("test333", "사용자($userId)가 상품을($productDocId)주문한 횟수: ${orderCount}")

            return orderCount

        }

    }
        // sehoon userDocID로 리뷰 정보 가져오기
        suspend fun getAllReview(): List<MyReviewParent> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("reviewData")
            val reviewResult = mutableListOf<MyReviewParent>()

            try {
                val reviewSnapshot = collectionReference
                    .orderBy("reviewTimeStamp", Query.Direction.DESCENDING) // Firestore에서 직접 정렬
                    .get()
                    .await()

                Log.d("Firestore", "총 가져온 문서 개수: ${reviewSnapshot.documents.size}")

                for (document in reviewSnapshot) {
                    Log.d("Firestore", "문서 데이터: ${document.data}")

                    val review = document.toObject(MyReviewParent::class.java)
                    reviewResult.add(review)
                    Log.d("Firestore", "변환된 객체: $review")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return reviewResult
        }

        // sehoon userDocID로 리뷰 정보 가져오기
        suspend fun getUserReview(userDocId: String): List<MyReviewParent> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("reviewData")
            val reviewResult = mutableListOf<MyReviewParent>()

            try {
                val reviewSnapshot = collectionReference
                    .whereEqualTo("reviewCustomerDocId", userDocId)
                    .orderBy("reviewTimeStamp", Query.Direction.DESCENDING) // Firestore에서 직접 정렬
                    .get()
                    .await()

                Log.d("Firestore", "총 가져온 문서 개수: ${reviewSnapshot.documents.size}")

                for (document in reviewSnapshot) {
                    Log.d("Firestore", "문서 데이터: ${document.data}")

                    val review = document.toObject(MyReviewParent::class.java)
                    reviewResult.add(review)
                    Log.d("Firestore", "변환된 객체: $review")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return reviewResult
        }

        // sh reviewDocId가 일치하는 reviewData 제거
        suspend fun removeUserReview(reviewDocId: String): Boolean {
            val firebase = FirebaseFirestore.getInstance()
            val collectionReference = firebase.collection("reviewData")

            return try {
                val documents =
                    collectionReference.whereEqualTo("reviewDocId", reviewDocId).get().await()

                if (documents.isEmpty) {
                    return false
                }
                for (document in documents) {
                    collectionReference.document(document.id).delete().await()
                }
                true
            } catch (_: Exception) {
                false
            }
        }



}
