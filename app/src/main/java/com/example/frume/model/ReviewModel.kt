package com.example.frume.model

import com.example.frume.util.ReviewState
import com.example.frume.vo.ReviewVO
import com.google.firebase.Timestamp

// 리뷰 Model
class ReviewModel {
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
    var reviewState = ReviewState.REVIEW_STATE_VISIBLE // 1 : 노출 상태

    fun toReviewVO(): ReviewVO {
        val reviewVO = ReviewVO()
        reviewVO.reviewDocId = reviewDocId
        reviewVO.reviewProductDocId = reviewProductDocId
        reviewVO.reviewCustomerDocId = reviewCustomerDocId
        reviewVO.reviewTitle = reviewTitle
        reviewVO.reviewContent = reviewContent
        reviewVO.reviewImagesPath = reviewImagesPath
        reviewVO.reviewRatingPoint = reviewRatingPoint
        reviewVO.reviewTimeStamp = reviewTimeStamp
        reviewVO.reviewAnswer = reviewAnswer
        reviewVO.reviewState = reviewState.num
        return reviewVO
    }


}