package com.example.frume.service

import com.example.frume.data.Product
import com.example.frume.model.ProductModel
import com.example.frume.repository.ProductRepository

class ProductService {
    companion object {
        // 카테고리별 품목 가져오기
        suspend fun gettingProductByCategory(productCategoryType: String): MutableList<ProductModel> {

            // 상품정보를 가져온다.
            val productModelList = mutableListOf<ProductModel>()
            val productVOList = ProductRepository.gettingProductByCategory(productCategoryType)

            productVOList.forEach {
                productModelList.add(it.toProductModel())
            }
            return productModelList
        }

        // sehoon productDocId로 제품의 정보를 가져온다
        suspend fun getProductInfo(productId: String): Product {
            val product = ProductRepository.getProductInfo(productId)

            return product
        }


        // 상품 문서 ID로 상품 한개 Model 가져오기 hj
        suspend fun gettingProductOneByDocId(productDocID: String): ProductModel {
            val selectedProductVO = ProductRepository.gettingProductOneByDocId(productDocID)
            return selectedProductVO.toProductModel()
        }

    }
}
