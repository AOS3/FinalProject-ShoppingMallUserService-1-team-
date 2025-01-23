package com.example.frume.service

import android.util.Log
import com.example.frume.model.ProductModel
import com.example.frume.repository.ProductRepository
import com.example.frume.vo.ProductVO

class ProductService {
    companion object{
        // 카테고리별 품목 가져오기
        suspend fun gettingProductByCategory(productCategoryType: String) : MutableList<ProductModel>{
            Log.d("test100","ProductService : gettingProductByCategory")

            // 상품정보를 가져온다.
            val productModelList = mutableListOf<ProductModel>()
            val productVOList = ProductRepository.gettingProductByCategory(productCategoryType)

            productVOList.forEach {
                productModelList.add(it.toProductModel())
            }

            return productModelList
        }

        // 홈화면 recyclerview 전체 가져오기
        suspend fun gettingProductAll(): MutableList<ProductModel> {
            Log.d("test100","ProductService : gettingProductAll()")

            // 상품정보를 가져온다.
            val productModelList = mutableListOf<ProductModel>()
            val productVOList = ProductRepository.gettingProductAll()

            productVOList.forEach {
                productModelList.add(it.toProductModel())
                Log.d("test100","")
            }
            return productModelList
        }


        // 홈화면 탭바 별로 가져오기(신제품, 특가, 베스트, 1인, 패키지)
    }
}
