package com.example.frume.fragment.user_fragment.product_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frume.data.MyReviewParent
import com.example.frume.repository.ReviewRepository
import com.example.frume.service.ReviewService
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

    // 리뷰 작성 버튼 가시성 제어
    private val _isReviewButtonVisible = MutableLiveData<Boolean>()
    val isReviewButtonVisible: LiveData<Boolean> get() = _isReviewButtonVisible




    fun loadReview(productDocId: String) {
        viewModelScope.launch {
            val review = repository.getProductIdReview(productDocId)
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

    fun removeReview(reviewDocId: String,productDocId: String) {
        viewModelScope.launch {
            val isRemove = repository.removeUserReview(reviewDocId)
            _isRemove.postValue(isRemove)

            if (isRemove) {
                loadReview(productDocId)
            }
        }
    }

    fun resetRemove() {
        _isRemove.postValue(null)
    }

    fun checkReviewButtonVisibility(productDocId: String, userId: String) {
        viewModelScope.launch {
            val myReviewCount = ReviewService.getMyReviewCountByProduct(productDocId, userId)
            val orderCount = ReviewService.getMyOrderCountByProduct(productDocId, userId)

            _isReviewButtonVisible.value = myReviewCount < orderCount
        }
    }

}