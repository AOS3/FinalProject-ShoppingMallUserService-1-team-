package com.example.frume.model

import com.example.frume.util.AdminSate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.model.Values

class AdminModel {
    // 관리자 문서 ID
    var adminId = ""
    // 관리자 코드
    var adminCode = ""
    // 관리자 매출 상태
    var adminState = AdminSate.ADMIN_STATE_NORMAL // 정상 : 1 , 비정상 2
    // 관리자 등록 시간
    var adminTimeStamp = Timestamp.now()
}
