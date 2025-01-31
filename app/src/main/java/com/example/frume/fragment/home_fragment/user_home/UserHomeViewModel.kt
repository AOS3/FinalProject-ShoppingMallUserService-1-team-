package com.example.frume.fragment.home_fragment.user_home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data.TempBanner
import com.example.frume.model.ProductModel
import com.example.frume.service.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserHomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> get() = _products

    private val _banners = MutableLiveData<List<TempBanner>>()
    val banner: LiveData<List<TempBanner>> get() = _banners

    // fragment에서 받은 값
    private val _data = MutableLiveData<Int>()
    val data: LiveData<Int> get() = _data

    init {
        //loadProducts()
        getBannerProduct()
    }

    fun updateData(newValue: Int) {
        _data.value = newValue
        Log.d("_data", _data.value.toString())
        loadProducts(newValue) // 데이터를 업데이트하면서 카테고리 필터링
    }

//    private fun loadProducts() {
//        viewModelScope.launch {
//            try {
//                val products = withContext(Dispatchers.IO) {
//                    ProductService.gettingProductAll().map { productModel ->
//                        TempProduct(
//                            productDocId = productModel.productDocId,
//                            productImgResourceId = R.drawable.btn_background,
//                            productName = productModel.productName,
//                            productPrice = productModel.productPrice,
//                            productDescription = productModel.productDescription,
//                            productCategory = productModel.productHomeCategory
//                        )
//                    }
//
//
//                }
//                _products.value = products
//            } catch (e: Exception) {
//                Log.e("UserHomeViewModel", "Error loading products: ${e.message}", e)
//            }
//        }
//    }

    // 카테고리에 따라 제품 데이터 로드
    private fun loadProducts(categoryNumber: Int) {
        viewModelScope.launch {
            try {
                val filteredProducts = withContext(Dispatchers.IO) {
                    // 모든 제품 가져오기
                    val allProducts = ProductService.gettingProductAll()

                    Log.d("test111", "All products: $allProducts")

                    // 카테고리에 따른 필터링 및 TempProduct로 변환
                    allProducts.filter { productModel ->
                        Log.d(
                            "test111",
                            "Filtering: ${productModel.productHomeCategory} == $categoryNumber"
                        )
                        productModel.productHomeCategory == categoryNumber // 필터 조건
                    }.map { productModel ->
                       /* TempProduct(
                            productDocId = productModel.productDocId,
                            productImgResourceId = R.drawable.btn_background, // 예제 이미지 리소스 ID
                            productName = productModel.productName,
                            productPrice = productModel.productPrice,
                            productDescription = productModel.productDescription,
                            productCategory = productModel.productHomeCategory
                        )*/
                    }
                }

              /*  // 필터링된 데이터를 LiveData에 업데이트
                _products.value = filteredProducts*/
                Log.d("test111", "Filtered products: $filteredProducts")
            } catch (e: Exception) {
                Log.e("UserHomeViewModel", "Error loading products: ${e.message}", e)
            }
        }
    }


    fun getBannerProduct(): List<TempBanner> {
        val banners = Storage.bannerList
        _banners.value = banners
        return banners
    }
}
