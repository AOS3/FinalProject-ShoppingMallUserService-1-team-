package com.example.frume.fragment.user_fragment.product_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.data.MyReviewParent
import com.example.frume.repository.ReviewRepository
import kotlinx.coroutines.launch

class ProductReviewViewModel(
    private val repository: ReviewRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<MyReviewParent>>()
    val items: LiveData<List<MyReviewParent>> = _items

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> get() = _currentPage

    private val _maxPage = MutableLiveData(0)
    val maxPage: LiveData<Int> get() = _maxPage

    private val pageSize = 10
    private val allReviews = mutableListOf<MyReviewParent>()


    private val _isRemove = MutableLiveData<Boolean?>()
    val isRemove: LiveData<Boolean?> = _isRemove


    init {
        loadReview()
    }

    fun loadReview() {
        viewModelScope.launch {
            val review = repository.getAllReview()
            allReviews.clear()
            allReviews.addAll(review)

            _maxPage.value = (allReviews.size + pageSize - 1) / pageSize
            loadPage(0)

            _items.postValue(review)
        }
    }

    fun loadPage(page: Int) {
        val start = page * pageSize
        val end = (start + pageSize).coerceAtMost(allReviews.size)

        if (start < end) {
            _items.value = allReviews.subList(start, end)
            _currentPage.value = page
        }
    }

    fun nextPage() {
        val nextPage = (_currentPage.value ?: 0) + 1
        if (nextPage * pageSize < allReviews.size) {
            loadPage(nextPage)
        }
    }

    fun previousPage() {
        val prevPage = (_currentPage.value ?: 0) - 1
        if (prevPage >= 0) {
            loadPage(prevPage)
        }
    }

    fun removeReview(reviewDocId: String) {
        viewModelScope.launch {
            val isRemove = repository.removeUserReview(reviewDocId)
            _isRemove.postValue(isRemove)

            if (isRemove) {
                loadReview()
            }
        }
    }

    fun resetRemove() {
        _isRemove.postValue(null)
    }
}