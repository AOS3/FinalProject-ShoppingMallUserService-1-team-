package com.example.frume.model

import com.example.frume.util.InquiryIsOpenBoolType
import com.example.frume.util.InquiryIsSecretBoolType
import com.example.frume.vo.InquiryVO
import com.google.firebase.Timestamp

// 문의 Model
class InquiryModel {
    // 문의 문서 ID
    var inquiryDocId = ""

    // 문의할 상품 ID
    var inquiryProductDocId = ""

    // 문의 작성자 ID
    var inquiryCustomerDocId = ""

    // 문의 제목
    var inquiryTitle = ""

    // 문의 내용
    var inquiryContent = ""

    // 문의 사진 경로 3장
    var inquiryImagesPath = mutableListOf<String>()

    // 답변 내용
    var inquiryAnswer = ""

    // 비밀글 여부
    var inquiryIsSecret = InquiryIsSecretBoolType.INQUIRY_IS_SECRET_TRUE // true

    // 공개상태
    var inquiryBoolState = InquiryIsOpenBoolType.INQUIRY_IS_OPEN_TRUE // true

    // 문의 등록 시간
    var inquiryTimeStamp = Timestamp.now()

    fun toInquiryVO(): InquiryVO {
        val inquiryVO = InquiryVO()

        inquiryVO.inquiryDocId = inquiryDocId
        inquiryVO.inquiryProductDocId = inquiryProductDocId
        inquiryVO.inquiryCustomerDocId = inquiryCustomerDocId
        inquiryVO.inquiryTitle = inquiryTitle
        inquiryVO.inquiryContent = inquiryContent
        inquiryVO.inquiryImagesPath = inquiryImagesPath
        inquiryVO.inquiryAnswer = inquiryAnswer
        inquiryVO.inquiryIsSecret = inquiryIsSecret.bool
        inquiryVO.inquiryBoolState = inquiryBoolState.bool
        inquiryVO.inquiryTimeStamp = inquiryTimeStamp

        return inquiryVO
    }

}