package com.example.frume.fragment.my_info.review

import com.example.frume.data.MyReviewParent

interface ReviewClickListener {
    fun onClickDelete(review: MyReviewParent)
}