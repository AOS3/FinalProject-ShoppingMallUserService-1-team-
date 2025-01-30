package com.example.frume.service

import com.example.frume.model.ReviewModel
import com.example.frume.repository.ReviewRepository

class ReviewService {
    companion object{

        // 내 후기 목록 가져오기
        suspend fun gettingMyReviewList(customerUserDocId: String): MutableList<ReviewModel> {
            val result = mutableListOf<ReviewModel>()
            val reviewVoList = ReviewRepository.gettingMyReviewList(customerUserDocId)
            reviewVoList.forEach{
                result.add(it.toReviewModel())
            }
            return result
        }
    }
}

