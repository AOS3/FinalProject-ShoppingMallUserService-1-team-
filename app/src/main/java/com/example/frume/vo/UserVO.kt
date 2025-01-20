package com.example.frume.vo

import com.example.frume.model.DeliveryAddressModel
import com.example.frume.util.CustomerUserGender
import com.example.frume.util.CustomerUserState
import com.google.firebase.Timestamp

class UserVO {
    // 문서 ID
    var customerUserDocId = ""

    // 아이디
    var customerUserId = ""

    // 비밀번호
    var customerUserPw = ""

    // 이메일
    var customerUserEmail = ""

    // 이름
    var customerUserName = ""

    // 나이
    var customerUserAge = 0

    // 성별
    var customerUserGenderIsMale = 1 // 1 : 남자

    // 휴대폰 번호
    var customerUserPhoneNumber = ""

    // 회원 주소
    var customerUserAddress = mutableMapOf(
        "BasicAddress" to "",
        "DetailedAddress" to "",
        "PostNumber" to ""
    )


    // 배송지 items
    var customerUserAddressItems = mutableListOf<DeliveryAddressModel>()

    // 적립금
    var customerUserReward = 0

    // 회원 상태
    var customerUserState = 1 // 1 : 활동중

    // 등록 날짜
    var customerUserTimeStamp = Timestamp.now()

    // 로컬 토큰
    var customerUserLocalToken = ""

    // 카카오 토큰
    var customerUserKakaoToken = ""
}