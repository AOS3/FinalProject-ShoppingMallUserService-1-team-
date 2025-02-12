package com.example.frume.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.frume.fragment.product.UserProductInfoDescriptionFragment
import com.example.frume.fragment.product.detail_screen.UserProductInfoDetailFragment
import com.example.frume.fragment.product.UserProductInfoInquiryFragment
import com.example.frume.fragment.product.review_screen.UserProductInfoReviewFragment

class ProductTabAdapter(
    fragment: Fragment,
    private val productDetails: List<String>,
    private val productDocId: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = productDetails.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserProductInfoDescriptionFragment.newInstance(productDocId)
            1 -> UserProductInfoDetailFragment.newInstance(productDocId)
            2 -> UserProductInfoReviewFragment.newInstance(productDocId)
            else -> UserProductInfoInquiryFragment.newInstance(productDocId)
        }
    }
}