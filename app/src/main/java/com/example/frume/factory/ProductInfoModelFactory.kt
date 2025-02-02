package com.example.frume.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frume.fragment.user_fragment.product_info.ProductInfoViewModel
import com.example.frume.repository.ProductRepository

class ProductInfoModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductInfoViewModel::class.java)) {
            return ProductInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}