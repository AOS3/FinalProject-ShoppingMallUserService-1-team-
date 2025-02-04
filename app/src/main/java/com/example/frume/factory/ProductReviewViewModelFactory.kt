package com.example.frume.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frume.fragment.product.review_screen.ProductReviewViewModel
import com.example.frume.repository.ReviewRepository

class ProductReviewViewModelFactory(
    private val repository: ReviewRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductReviewViewModel::class.java)) {
            return ProductReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}