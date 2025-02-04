package com.example.frume.fragment.my_info


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.activity.HomeActivity
import com.example.frume.data.MyReviewParent
import com.example.frume.databinding.FragmentUserInfoReviewBinding
import com.example.frume.factory.ReviewViewModelFactory
import com.example.frume.fragment.my_info.review.MyReviewParentAdapter
import com.example.frume.fragment.my_info.review.ReviewClickListener
import com.example.frume.fragment.my_info.review.ReviewViewModel
import com.example.frume.repository.ReviewRepository
import com.example.frume.util.showDialog
import com.example.frume.util.showToast


class UserInfoReviewFragment : Fragment(), ReviewClickListener {
    private var _binding: FragmentUserInfoReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by lazy {
        val repository = ReviewRepository()
        val factory = ReviewViewModelFactory(repository)
        ViewModelProvider(this, factory)[ReviewViewModel::class.java]
    }
    private lateinit var adapter: MyReviewParentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_review, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        settingToolbar()
        setLayout()
        observeData()
    }

    private fun settingToolbar() {
        binding.toolbarUserInfoReview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setLayout() {
        val userDocId = activity as HomeActivity
        adapter = MyReviewParentAdapter(userDocId.loginUserDocumentId, this)
        binding.recyclerViewUserInfoReview.adapter = adapter
    }

    private fun observeData() {
        val userDocId = activity as HomeActivity
        viewModel.loadReview(userDocId.loginUserDocumentId)
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitReview(it)
        }
    }


    override fun onClickDelete(review: MyReviewParent) {
        val userDocId = activity as HomeActivity
        requireContext().showDialog(
            title = "리뷰 삭제",
            msg = "선택하신 리뷰를 삭제하시겠습니까?",
            pos = "삭제",
            nega = "취소"
        ) { result ->
            if (result) {
                viewModel.removeReview(review.reviewDocId!!, userDocId.loginUserDocumentId)
            }
        }
        viewModel.isRemove.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    requireContext().showToast("삭제되었습니다.")
                } else {
                    requireContext().showToast("다시 확인해주세요.")
                }
                viewModel.resetRemove()
            }
        }
    }
}