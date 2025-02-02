package com.example.frume.fragment.user_fragment.product_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.data.Product
import com.example.frume.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _items = MutableLiveData<Product>()
    val items: LiveData<Product> = _items


    fun loadProduct(productDocId: String) {
        viewModelScope.launch {
            val product = repository.getProductInfo(productDocId)
            _items.postValue(product)
        }
    }
}