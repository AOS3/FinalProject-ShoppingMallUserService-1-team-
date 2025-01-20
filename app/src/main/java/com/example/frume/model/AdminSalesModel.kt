package com.example.frume.model

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

    // 판매 상태
    var salesOrderState = 0

    // 주 카테고리
    var salesCategory1 = ""

    // 부 카테고리
    var salesCategory2 = ""

}