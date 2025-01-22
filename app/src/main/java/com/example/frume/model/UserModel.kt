package com.example.frume.model

import com.example.frume.util.CustomerUserGender
import com.example.frume.util.CustomerUserState
import com.example.frume.vo.UserVO
import com.google.firebase.Timestamp

// 유저 Model
class UserModel {
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
    var customerUserGender = CustomerUserGender.CUSTOMER_USER_GENDER_MALE // 1 : 남자

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
    var customerUserState = CustomerUserState.CUSTOMER_USER_STATE_ACTIVE // 1 : 활동중

    // 등록 날짜
    var customerUserTimeStamp = Timestamp.now()

    // 로컬 토큰
    var customerUserLocalToken = ""

    // 카카오 토큰
    var customerUserKakaoToken = ""
    // 기본 배송지 주소
    //var customerUserBasicAddress=""
    //배송지 db에 기본배송지 여부가 들어가서 제거함

    fun toUserVO(): UserVO {

        val userVO = UserVO()
        userVO.customerUserDocId = customerUserDocId
        userVO.customerUserId = customerUserId
        userVO.customerUserPw = customerUserPw
        userVO.customerUserEmail = customerUserEmail
        userVO.customerUserName = customerUserName
        userVO.customerUserAge = customerUserAge
        userVO.customerUserGender = customerUserGender.num
        userVO.customerUserPhoneNumber = customerUserPhoneNumber
        userVO.customerUserAddress = customerUserAddress
        userVO.customerUserAddressItems = customerUserAddressItems
        userVO.customerUserReward = customerUserReward
        userVO.customerUserState = customerUserState.num
        userVO.customerUserTimeStamp = customerUserTimeStamp
        userVO.customerUserLocalToken = customerUserLocalToken
        userVO.customerUserKakaoToken = customerUserKakaoToken

        return userVO
    }
}


