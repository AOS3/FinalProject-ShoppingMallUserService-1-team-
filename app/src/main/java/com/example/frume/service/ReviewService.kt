package com.example.frume.service

import android.util.Log
import com.example.frume.model.ReviewModel
import com.example.frume.repository.ReviewRepository

class ReviewService {
    companion object {
        suspend fun setUserReview(reviewModel: ReviewModel): String {
            // 데이터를 VO에 담아준다.
            val reviewVO = reviewModel.toReviewVO()
            // 저장하는 메서드를 호출한다.
            val reviewDocId = ReviewRepository.setUserReview(reviewVO)
            return reviewDocId
        }

        suspend fun getUserReviewCount(userDocId: String): Int {
            val result = ReviewRepository.getMyReviewCount(userDocId)

            // 리뷰 개수를 로그로 출력
            Log.d("test200", "getUserReviewCount에서 받은 리뷰 개수: $result")

            return result
        }


        // 특정 상품에 대한 내 리뷰 개수
        suspend fun getMyReviewCountByProduct(productDocId: String, userId: String) :Int{
            val result = ReviewRepository.getMyReviewCountByProduct(productDocId, userId)
            return result
        }

        // 특정 상품 주문 횟수
        suspend fun getMyOrderCountByProduct(productDocId: String, userId: String):Int{
            val result = ReviewRepository.getMyOrderCountByProduct(productDocId, userId)
            return result
        }
    }
}