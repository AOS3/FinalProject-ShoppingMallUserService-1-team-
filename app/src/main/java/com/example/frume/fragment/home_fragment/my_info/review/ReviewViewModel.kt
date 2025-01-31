package com.example.frume.fragment.home_fragment.my_info.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.data.MyReviewParent
import com.example.frume.repository.ReviewRepository
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val repository: ReviewRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<MyReviewParent>>()
    val items: LiveData<List<MyReviewParent>> = _items

    private val _isRemove = MutableLiveData<Boolean?>()
    val isRemove: LiveData<Boolean?> = _isRemove


    fun loadReview(userDocId: String) {
        viewModelScope.launch {
            val review = repository.getUserReview(userDocId)
            _items.postValue(review)
        }
    }

    fun removeReview(reviewDocId: String, userDocId: String) {
        viewModelScope.launch {
            val isRemove = repository.removeUserReview(reviewDocId)
            _isRemove.postValue(isRemove)

            if (isRemove) {
                loadReview(userDocId)
            }
        }
    }

    fun resetRemove() {
        _isRemove.postValue(null)
    }
}