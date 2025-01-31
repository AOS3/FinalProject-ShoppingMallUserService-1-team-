package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.data.MyReviewParent
import com.example.frume.databinding.FragmentUserProductInfoReviewBinding
import com.example.frume.factory.ProductReviewViewModelFactory
import com.example.frume.fragment.home_fragment.my_info.review.MyReviewParentAdapter
import com.example.frume.fragment.home_fragment.my_info.review.ReviewClickListener
import com.example.frume.home.HomeActivity
import com.example.frume.repository.ReviewRepository
import com.example.frume.util.showDialog
import com.example.frume.util.showToast


class UserProductInfoReviewFragment : Fragment(), ReviewClickListener {
    private var _binding: FragmentUserProductInfoReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MyReviewParentAdapter

    private val viewModel: ProductReviewViewModel by lazy {
        val repository = ReviewRepository()
        val factory = ProductReviewViewModelFactory(repository)
        ViewModelProvider(this, factory)[ProductReviewViewModel::class.java]
    }

    private var productDocId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productDocId = it.getString(ARG_PRODUCT_DOC_ID)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadReview()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_review, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        settingRecyclerView()
        observeData()
        onClickBtn()
    }

    private fun settingRecyclerView() {
        val userDocId = activity as HomeActivity
        adapter = MyReviewParentAdapter(userDocId.loginUserDocumentId, this)
        binding.recyclerViewUserProductInfoReview.adapter = adapter
    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitReview(it)
        }
        viewModel.currentPage.observe(viewLifecycleOwner) { page ->
            binding.textViewUserProductInfoReviewNowPageNumber.text = "${page + 1}"
        }
        viewModel.maxPage.observe(viewLifecycleOwner) { maxPage ->
            binding.textViewUserProductInfoReviewMaxPageNumber.text = "$maxPage"
        }
    }


    // sehoon 클릭 메서드
    private fun onClickBtn() {
        binding.buttonUserProductInfoReview.setOnClickListener {
            val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserProductInfoWriteReview(productDocId!!)
            findNavController().navigate(action)
        }
        binding.imageViewUserProductInfoReviewForward.setOnClickListener {
            viewModel.nextPage()
        }
        binding.imageViewUserProductInfoReviewBack.setOnClickListener {
            viewModel.previousPage()
        }
    }

    companion object {
        private const val ARG_PRODUCT_DOC_ID = "arg_number"
        fun newInstance(productDocId: String): UserProductInfoReviewFragment {
            return UserProductInfoReviewFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_DOC_ID, productDocId)

                }
            }
        }

    }

    override fun onClickDelete(review: MyReviewParent) {
        requireContext().showDialog(
            title = "리뷰 삭제",
            msg = "선택하신 리뷰를 삭제하시겠습니까?",
            pos = "삭제",
            nega = "취소"
        ) { result ->
            if (result) {
                viewModel.removeReview(review.reviewDocId!!)
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