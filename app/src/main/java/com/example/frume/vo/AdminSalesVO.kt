package com.example.frume.vo

import com.example.frume.model.AdminSalesModel
import com.example.frume.util.AdminSalesState
import com.google.firebase.Timestamp

class AdminSalesVO {
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
    var salesOrderState = 1 // 1 : 정상, 2 : 비정상

    // 주 카테고리
    var salesCategory1 = ""

    // 부 카테고리
    var salesCategory2 = ""

    fun toAdminSalesModel(): AdminSalesModel {
        val adminSalesModel = AdminSalesModel()

        adminSalesModel.salesDocId = salesDocId
        adminSalesModel.orderProductId = orderProductId
        adminSalesModel.orderId = orderId
        adminSalesModel.salesOrderCost = salesOrderCost
        adminSalesModel.salesOrderDate = salesOrderDate
        adminSalesModel.salesCategory1 = salesCategory1
        adminSalesModel.salesCategory2 = salesCategory2

        when(salesOrderState){
            AdminSalesState.ADMIN_SALES_STATE_NORMAL.num->{adminSalesModel.salesOrderState= AdminSalesState.ADMIN_SALES_STATE_NORMAL}
            AdminSalesState.ADMIN_SALES_STATE_ABNORMAL.num->{adminSalesModel.salesOrderState= AdminSalesState.ADMIN_SALES_STATE_ABNORMAL}
        }


        return adminSalesModel
    }
}