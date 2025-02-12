package com.example.frume.model

import com.example.frume.util.AdminSalesState
import com.example.frume.vo.AdminSalesVO
import com.google.firebase.Timestamp

// 관리자 매출 Model
class AdminSalesModel {
    // 매출 문서 ID
    var salesDocId = ""

    // 주문 상품 ID
    var orderProductId = ""

    // 주문 ID
    var orderId = ""

    // 매출금액
    var salesOrderCost = 0

    // 매출날짜
    var salesOrderDate = Timestamp.now()

    // 매출 상태
    var salesOrderState = AdminSalesState.ADMIN_SALES_STATE_NORMAL // 1 정상

    // 주 카테고리
    var salesCategory1 = ""

    // 부 카테고리
    var salesCategory2 = ""

    // 세부 카테고리
    var salesCategory3 = ""

    fun toAdminSalesVO(): AdminSalesVO {
        val adminSalesVo = AdminSalesVO()

        adminSalesVo.salesDocId = salesDocId
        adminSalesVo.orderProductId = orderProductId
        adminSalesVo.orderId = orderId
        adminSalesVo.salesOrderCost = salesOrderCost
        adminSalesVo.salesOrderDate = salesOrderDate
        adminSalesVo.salesOrderState = salesOrderState.num
        adminSalesVo.salesCategory1 = salesCategory1
        adminSalesVo.salesCategory2 = salesCategory2
        adminSalesVo.salesCategory3 = salesCategory3

        return adminSalesVo
    }

}