package com.example.frume.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frume.fragment.home.home_tab_second_screen.HomeSecondViewModel
import com.example.frume.repository.ProductRepository

class HomeSecondViewModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeSecondViewModel::class.java)) {
            return HomeSecondViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}