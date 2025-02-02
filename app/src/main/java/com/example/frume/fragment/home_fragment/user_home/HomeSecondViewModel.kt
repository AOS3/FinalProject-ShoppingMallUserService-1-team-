package com.example.frume.fragment.home_fragment.user_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.data.Product
import com.example.frume.repository.ProductRepository
import kotlinx.coroutines.launch

class HomeSecondViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<Product>>()
    val items: LiveData<List<Product>> = _items


    fun loadProduct(category: String) {
        viewModelScope.launch {
            val review = repository.gettingProductCategory(category)
            _items.postValue(review)
        }
    }

    fun loadBestProduct() {
        viewModelScope.launch {
            val bestProduct = repository.getBestProductCategory()
            // 상위 30%만 추출
            val topPercentage = (bestProduct.size * 0.3).toInt().coerceAtLeast(1) // 최소 1개 보장
            val topProducts = bestProduct.take(topPercentage) // 리스트에서 상위 10% 추출

            _items.postValue(topProducts)
        }
    }

}