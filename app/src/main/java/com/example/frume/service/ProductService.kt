package com.example.frume.service

import android.util.Log
import com.example.frume.model.ProductModel
import com.example.frume.repository.ProductRepository
import com.example.frume.vo.ProductVO

class ProductService {
    companion object{
        // 카테고리별 품목 가져오기
        suspend fun gettingProductByCategory(productCategoryType: String) : MutableList<ProductModel>{

            // 상품정보를 가져온다.
            val productModelList = mutableListOf<ProductModel>()
            val productVOList = ProductRepository.gettingProductByCategory(productCategoryType)

            productVOList.forEach {
                productModelList.add(it.toProductModel())
            }
            return productModelList
        }

        suspend fun getProductInfo(productId: String): MutableList<ProductModel> {
            val productModelList = mutableListOf<ProductModel>()
            val productVOList = ProductRepository.getProductInfo(productId)

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

        // 상품 문서 ID로 상품 한개 Model 가져오기 hj
        suspend fun gettingProductOneByDocId(productDocID: String) :ProductModel{
            val selectedProductVO = ProductRepository.gettingProductOneByDocId(productDocID)
            return selectedProductVO.toProductModel()
        }
    }
}
