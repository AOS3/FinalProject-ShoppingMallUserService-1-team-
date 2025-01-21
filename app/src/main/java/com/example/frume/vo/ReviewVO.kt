package com.example.frume.vo

import com.example.frume.model.ReviewModel
import com.example.frume.util.ReviewState
import com.google.firebase.Timestamp

class ReviewVO {
    // 리뷰 문서 ID
    var reviewDocId = ""

    // 리뷰할 상품 문서 ID
    var reviewProductDocId = ""

    // 리뷰 작성자 문서 ID
    var reviewCustomerDocId = ""

    // 리뷰 제목
    var reviewTitle = ""

    // 제목 내용
    var reviewContent = ""

    // 리뷰 사진들 경로
    var reviewImagesPath = mutableListOf<String>()

    // 리뷰 별점
    var reviewRatingPoint = 0.0f

    // 리뷰 등록 시간
    var reviewTimeStamp = Timestamp.now()

    // 후기 답변
    var reviewAnswer = ""

    // 노출 상태
    var reviewState = 1 // 1 : 노출 상태


    fun toReviewModel(): ReviewModel {
        val reviewModel = ReviewModel()
        reviewModel.reviewDocId = reviewDocId
        reviewModel.reviewProductDocId = reviewProductDocId
        reviewModel.reviewCustomerDocId = reviewCustomerDocId
        reviewModel.reviewTitle = reviewTitle
        reviewModel.reviewContent = reviewContent
        reviewModel.reviewImagesPath = reviewImagesPath
        reviewModel.reviewRatingPoint = reviewRatingPoint
        reviewModel.reviewTimeStamp = reviewTimeStamp
        reviewModel.reviewAnswer = reviewAnswer
        when(reviewState){
            ReviewState.REVIEW_STATE_VISIBLE.num->{reviewModel.reviewState=ReviewState.REVIEW_STATE_VISIBLE}
            ReviewState.REVIEW_STATE_HIDDEN.num->{reviewModel.reviewState = ReviewState.REVIEW_STATE_HIDDEN}
        }
        return reviewModel
    }
}