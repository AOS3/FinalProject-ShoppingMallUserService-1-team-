package com.example.frume.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frume.fragment.product.detail_screen.ProductDetailViewModel
import com.example.frume.repository.ProductRepository

class ProductDetailModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            return ProductDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}