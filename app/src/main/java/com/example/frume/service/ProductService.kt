package com.example.frume.service

import android.util.Log
import com.example.frume.model.ProductModel
import com.example.frume.repository.ProductRepository

class ProductService {
    companion object{
        // 카테고리별 품목 가져오기
        suspend fun gettingProductByCategory(productCategoryType: String) : MutableList<ProductModel>{
            // Log.d("test100","ProductService : gettingProductByCategory")

            // 상품정보를 가져온다.
            val productModelList = mutableListOf<ProductModel>()
            val productVOList = ProductRepository.gettingProductByCategory(productCategoryType)

            productVOList.forEach {
                productModelList.add(it.toProductModel())
            }

            return productModelList
        }
    }
}