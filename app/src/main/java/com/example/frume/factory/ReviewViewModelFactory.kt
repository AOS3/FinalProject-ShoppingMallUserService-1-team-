package com.example.frume.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frume.fragment.user_info.my_review_screen.ReviewViewModel
import com.example.frume.repository.ReviewRepository

class ReviewViewModelFactory(
    private val repository: ReviewRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}