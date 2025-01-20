package com.example.frume.model

import com.google.firebase.Timestamp

class AdminModel {
    // 관리자 문서 ID
    var adminId = ""
    // 관리자 코드
    var adminCode = ""
    // 관리자 상태
    var adminState = 0
    // 관리자 등록 시간
    var adminTimeStamp = Timestamp.now()

}