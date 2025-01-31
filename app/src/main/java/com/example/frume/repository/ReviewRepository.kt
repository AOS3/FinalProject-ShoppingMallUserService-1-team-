package com.example.frume.repository

import android.util.Log
import com.example.frume.data.MyReviewParent
import com.example.frume.vo.ReviewVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

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
            val documents = collectionReference.whereEqualTo("reviewDocId", reviewDocId).get().await()

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
