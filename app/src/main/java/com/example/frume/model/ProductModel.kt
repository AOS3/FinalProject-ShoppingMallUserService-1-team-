package com.example.frume.model

import com.example.frume.util.ProductSellingState
import com.example.frume.util.ProductType
import com.example.frume.vo.ProductVO
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
    var productType = ProductType.PRODUCT_TYPE_FRESH // 1 : 신선 과일

    // 식품용량
    var productVolume = 0

    // 판매 상태
    var productSellingState = ProductSellingState.PRODUCT_STATE_NORMAL // 1: 정상

    // 등록 시간
    var productTimeStamp = Timestamp.now()

    // 안드감
    // 판매량
    var productSalesCount = 0

    // 세부 카테고리
    var productCategory3 = ""


    fun toProductVO(): ProductVO {
        val productVO = ProductVO()

        productVO.productDocId = productDocId
        productVO.productName = productName
        productVO.productPrice = productPrice
        productVO.productCategory1 = productCategory1
        productVO.productCategory2 = productCategory2
        productVO.productDescription = productDescription
        productVO.productVariety = productVariety
        productVO.productUnit = productUnit
        productVO.productImages = productImages
        productVO.productType = productType.num
        productVO.productVolume = productVolume
        productVO.productSellingState = productSellingState.num
        productVO.productTimeStamp = productTimeStamp
        productVO.productSalesCount = productSalesCount
        productVO.productCategory3 = productCategory3

        return productVO

    }
}