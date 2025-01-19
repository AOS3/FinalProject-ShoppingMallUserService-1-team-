package com.example.frume.fragment.home_fragment.user_home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frume.data.Storage
import com.example.frume.data.TempBanner
import com.example.frume.data.TempProduct

class UserHomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<TempProduct>>()
    val product: LiveData<List<TempProduct>> get() = _products

    private val _banners = MutableLiveData<List<TempBanner>>()
    val banner: LiveData<List<TempBanner>> get() = _banners

    // fragment에서 받은 값
    private val _data = MutableLiveData<Int>()
    val data: LiveData<Int> get() = _data

    init {
        getTempProduct()
        getBannerProduct()
    }

    fun updateData(newValue: Int) {
        _data.value = newValue
        Log.d("_data", _data.value.toString())
    }



    private fun getTempProduct() {
        val products = Storage.productList
        _products.value = products
    }

    fun getBannerProduct(): List<TempBanner> {
        val banners = Storage.bannerList
        _banners.value = banners
        return banners
    }
}


