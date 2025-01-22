package com.example.frume.vo

import com.example.frume.model.ProductModel
import com.example.frume.util.ProductSellingState
import com.example.frume.util.ProductType
import com.google.firebase.Timestamp

class ProductVO {
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
    var productType = 1 // 1 : 신선 과일

    // 식품용량
    var productVolume = 0

    // 판매 상태
    var productSellingState = 1 // 1: 정상

    // 등록 시간
    var productTimeStamp = Timestamp.now()

    // 안드감

    // 판매량
    var productSalesCount = 0

    // 세부 카테고리
    var productCategory3 = ""


    fun toProductModel(): ProductModel {
        val productModel = ProductModel()

        productModel.productDocId = productDocId
        productModel.productName = productName
        productModel.productPrice = productPrice
        productModel.productCategory1 = productCategory1
        productModel.productCategory2 = productCategory2
        productModel.productDescription = productDescription
        productModel.productVariety = productVariety
        productModel.productUnit = productUnit
        productModel.productImages = productImages
        productModel.productVolume = productVolume
        productModel.productTimeStamp = productTimeStamp
        productModel.productSalesCount = productSalesCount
        productModel.productCategory3 = productCategory3

        when(productType){
            ProductType.PRODUCT_TYPE_FRESH.num->{productModel.productType =ProductType.PRODUCT_TYPE_FRESH}
            ProductType.PRODUCT_TYPE_OTHER.num->{productModel.productType =ProductType.PRODUCT_TYPE_OTHER}
            ProductType.PRODUCT_TYPE_MIXED.num->{productModel.productType =ProductType.PRODUCT_TYPE_MIXED}
            ProductType.PRODUCT_TYPE_PROCESSED.num->{productModel.productType =ProductType.PRODUCT_TYPE_PROCESSED}
        }

        when (productSellingState) {
            ProductSellingState.PRODUCT_STATE_NORMAL.num->{ProductSellingState.PRODUCT_STATE_NORMAL}
            ProductSellingState.PRODUCT_STATE_ABNORMAL.num->{ProductSellingState.PRODUCT_STATE_ABNORMAL}
        }


        return productModel

    }
}