package com.example.frume.vo

import com.example.frume.model.AdminModel
import com.example.frume.util.AdminSate
import com.google.firebase.Timestamp

class AdminVO {
    // 관리자 문서 ID
    var adminId = ""
    // 관리자 코드
    var adminCode = ""
    // 관리자 상태
    var adminState = 0 // 0 : 정상 1: 탈퇴
    // 관리자 등록 시간
    var adminTimeStamp = Timestamp.now()

    fun toAdminModel(): AdminModel {
        val adminModel = AdminModel()

        adminModel.adminId = adminId
        adminModel.adminCode = adminCode
        adminModel.adminTimeStamp = adminTimeStamp

        when(adminState){
            AdminSate.ADMIN_STATE_NORMAL.number->{adminModel.adminState = AdminSate.ADMIN_STATE_NORMAL}
            AdminSate.ADMIN_STATE_SIGN_OUT.number->{adminModel.adminState = AdminSate.ADMIN_STATE_SIGN_OUT}
        }

        return adminModel

    }
}