package com.example.frume.listener

import com.example.frume.data.MyReviewParent

interface ReviewClickListener {
    fun onClickDelete(review: MyReviewParent)
}