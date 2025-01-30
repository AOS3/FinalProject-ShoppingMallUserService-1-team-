package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.ReviewVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewRepository {
    companion object {
        // 내 후기 목록 가져오기
        suspend fun gettingMyReviewList(customerUserDocId: String): MutableList<ReviewVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("reviewData")
            val result = collectionReference.whereEqualTo("reviewCustomerDocId", customerUserDocId).get().await()
            Log.d("test100", "ReviewRepository->gettingMyReview()->result : ${result.documents}")
            val userReviewVoList = result.toObjects(ReviewVO::class.java)

            return userReviewVoList
        }
    }
}