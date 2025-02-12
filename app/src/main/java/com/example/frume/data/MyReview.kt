package com.example.frume.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

data class MyReviewParent(
    var reviewTitle: String,
    var reviewContent: String,
    var reviewRatingPoint: Float,
    var reviewTimeStamp: Timestamp,
    var reviewImagesPath: List<String>,
    var reviewAnswer: String?,
    var reviewDocId: String?,
    var reviewCustomerDocId: String?,
    var reviewProductDocId: String?,  // 추가됨
    var reviewState: Int?
) {
    constructor() : this("noTitle", "noContent", 2.0f, Timestamp.now(), listOf(), "noAnswer", "", "", "", 1)
}


@IgnoreExtraProperties
data class MyReviewChild(
    @PropertyName("reviewImagesPath") val imagePath: String?,
    val pathToImage: String?,
) {
    constructor() : this(null, null)  // Firestore 직렬화/역직렬화용 기본 생성자 필요
}