package com.example.frume.service

import android.util.Log
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
            Log.d("ProductService", "getProductInfo() 호출됨, productId: $productId")

            val product = ProductRepository.getProductInfo(productId)

            Log.d("ProductService", "getProductInfo() 완료, 반환된 product: $product")


            return product
        }


        // 상품 문서 ID로 상품 한개 Model 가져오기 hj
        suspend fun gettingProductOneByDocId(productDocID: String): ProductModel {
            val selectedProductVO = ProductRepository.gettingProductOneByDocId(productDocID)
            return selectedProductVO.toProductModel()
        }

        suspend fun getSearchProductInfo(searchProduct: String): MutableList<ProductModel> {
            val productModelList = mutableListOf<ProductModel>()

            val productVOList = ProductRepository.getSearchProductInfo(searchProduct)
            productVOList.forEach {
                productModelList.add(it.toProductModel())
            }
            return productModelList
        }

    }


}
