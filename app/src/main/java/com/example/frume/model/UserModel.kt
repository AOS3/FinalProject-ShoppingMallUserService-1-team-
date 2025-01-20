package com.example.frume.model

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
    var customerUserGenderIsMale = true

    // 휴대폰 번호
    var customerUserPhoneNumber = ""

    // 회원 주소
    var customerUserAddress = ""

    // 배송지 items
    var customerUserAddressItems = mutableListOf<DeliveryAddressModel>()

    // 적립금
    var customerUserReward = 0

    // 회원 상태
    var customerUserState = 0

    // 등록 날짜
    var customerUserTimeStamp = Timestamp.now()

    // 로컬 토큰
    var customerUserLocalToken = ""

    // 카카오 토큰
    var customerUserKakaoToken = ""
    // 기본 배송지 주소
    //var customerUserBasicAddress=""
    //배송지 db에 기본배송지 여부가 들어가서 제거함


    /*    fun toUserVO() : UserVO{
            val userVO = UserVO()
            userVO.userId = userId
            userVO.userPw = userPw
            userVO.userAutoLoginToken = userAutoLoginToken
            userVO.userNickName = userNickName
            userVO.userAge = userAge
            userVO.userHobby1 = userHobby1
            userVO.userHobby2 = userHobby2
            userVO.userHobby3 = userHobby3
            userVO.userHobby4 = userHobby4
            userVO.userHobby5 = userHobby5
            userVO.userHobby6 = userHobby6
            userVO.userTimeStamp = userTimeStamp
            userVO.userState = userState.number

            return userVO
        }*/
}