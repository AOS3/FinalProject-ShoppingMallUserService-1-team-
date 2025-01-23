package com.example.frume.fragment.user_fragment.product_info

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

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