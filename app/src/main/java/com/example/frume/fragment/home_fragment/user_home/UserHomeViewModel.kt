package com.example.frume.fragment.home_fragment.user_home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.R
import com.example.frume.data.Product
import com.example.frume.data.Storage
import com.example.frume.data.TempBanner
import com.example.frume.model.ProductModel
import com.example.frume.service.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserHomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

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
    }

    fun getBannerProduct(): List<TempBanner> {
        val banners = Storage.bannerList
        _banners.value = banners
        return banners
    }
}
