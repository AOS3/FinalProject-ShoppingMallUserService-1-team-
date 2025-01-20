package com.example.frume.model

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
    var reviewImagesPath = ""

    // 리뷰 별점
    var reviewRatingPoint = 0.0f

    // 리뷰 등록 시간
    var reviewTimeStamp = Timestamp.now()

    // 후기 답변
    var reviewAnswer = ""

    // 노출 상태
    var reviewState = 0


}