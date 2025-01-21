package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data.TempReview
import com.example.frume.databinding.FragmentUserInfoReviewBinding
import com.example.frume.fragment.user_fragment.product_info.ProductReviewAdapter
import com.example.frume.fragment.user_fragment.product_info.ReviewClickListener
import kotlin.concurrent.thread


class UserInfoReviewFragment : Fragment(), ReviewClickListener {
    private var _binding: FragmentUserInfoReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductReviewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_review, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingToolbar()
        setLayout()
    }

    private fun settingToolbar() {
        binding.toolbarUserInfoReview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setLayout() {
        val myReview = Storage.reviewList
        adapter = ProductReviewAdapter(myReview.toMutableList(), this)
        binding.recyclerViewUserInfoReview.adapter = adapter

    }

    override fun onClickProductItem(reView: TempReview) {
        Toast.makeText(requireContext(), reView.reviewTitle, Toast.LENGTH_SHORT).show()
    }

}