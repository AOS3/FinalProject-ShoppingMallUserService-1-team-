package com.example.frume.model

import com.google.firebase.Timestamp

// 상품 Model
class ProductModel {
    // 상품 문서 ID
    var productDocId = ""

    // 상품명
    var productName = ""

    // 가격
    var productPrice = 0

    // 주카테고리
    var productCategory1 = ""

    // 부카테고리
    var productCategory2 = ""

    //상품 설명
    var productDescription = ""

    // 품종
    var productVariety = ""

    // 상품 단위
    var productUnit = ""

    // 사진들 경로 이름
    var productImages = mutableListOf<String>()

    // 식품유형
    var productType = ""

    // 식품용량
    var productVolume = 0

    // 판매 상태
    var productSellingState = 0

    // 등록 시간
    var productTimeStamp = Timestamp.now()


}