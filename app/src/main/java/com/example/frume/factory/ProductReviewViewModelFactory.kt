package com.example.frume.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frume.fragment.home_fragment.my_info.review.ReviewViewModel
import com.example.frume.fragment.user_fragment.product_info.ProductReviewViewModel
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