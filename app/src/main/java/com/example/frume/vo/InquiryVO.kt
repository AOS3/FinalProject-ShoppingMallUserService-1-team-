package com.example.frume.vo

import com.example.frume.model.InquiryModel
import com.example.frume.util.InquiryIsOpenBoolType
import com.example.frume.util.InquiryIsSecretBoolType
import com.google.firebase.Timestamp

class InquiryVO {
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
    var inquiryIsSecret = true

    // 공개여부
    var inquiryBoolState = true //

    // 문의 등록 시간
    var inquiryTimeStamp = Timestamp.now()

    fun toInquiryModel(): InquiryModel {
        val inquiryModel = InquiryModel()

        inquiryModel.inquiryDocId = inquiryDocId
        inquiryModel.inquiryProductDocId = inquiryProductDocId
        inquiryModel.inquiryCustomerDocId = inquiryCustomerDocId
        inquiryModel.inquiryTitle = inquiryTitle
        inquiryModel.inquiryContent = inquiryContent
        inquiryModel.inquiryImagesPath = inquiryImagesPath
        inquiryModel.inquiryAnswer = inquiryAnswer
        inquiryModel.inquiryTimeStamp = inquiryTimeStamp

        when(inquiryIsSecret){
            InquiryIsSecretBoolType.INQUIRY_IS_SECRET_TRUE.bool->{inquiryModel.inquiryIsSecret=InquiryIsSecretBoolType.INQUIRY_IS_SECRET_TRUE}
            InquiryIsSecretBoolType.INQUIRY_IS_SECRET_FALSE.bool->{inquiryModel.inquiryIsSecret=InquiryIsSecretBoolType.INQUIRY_IS_SECRET_FALSE}
            else->{}

        }
        when(inquiryBoolState){
            InquiryIsOpenBoolType.INQUIRY_IS_OPEN_TRUE.bool->{inquiryModel.inquiryBoolState= InquiryIsOpenBoolType.INQUIRY_IS_OPEN_TRUE}
            InquiryIsOpenBoolType.INQUIRY_IS_OPEN_FALSE.bool->{inquiryModel.inquiryBoolState= InquiryIsOpenBoolType.INQUIRY_IS_OPEN_FALSE}
            else->{}
        }

        return inquiryModel
    }

}