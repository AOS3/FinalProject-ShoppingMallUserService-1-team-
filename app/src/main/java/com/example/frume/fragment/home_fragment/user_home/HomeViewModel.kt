package com.example.frume.fragment.home_fragment.user_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.data.Product
import com.example.frume.repository.ProductRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<Product>>()
    val items: LiveData<List<Product>> = _items


    fun loadProduct() {
        viewModelScope.launch {
            val review = repository.gettingProductAll()
            _items.postValue(review)
        }
    }

}