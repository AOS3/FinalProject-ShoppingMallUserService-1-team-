package com.example.frume.fragment.product.review_screen

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
import com.example.frume.databinding.FragmentUserProductInfoReviewBinding
import com.example.frume.factory.ProductReviewViewModelFactory
import com.example.frume.adapter.MyReviewParentAdapter
import com.example.frume.fragment.product.info_screen.UserProductInfoFragmentDirections
import com.example.frume.listener.ReviewClickListener
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
        viewModel.loadReview(productDocId!!)
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
        onClickWriteButton()  // 버튼 설정 메서드
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
                viewModel.removeReview(review.reviewDocId!!,productDocId!!)
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

    // 특정 조건을 만족한 경우만 상품 후기 작성하기 버튼이 나타나도록 한다.
    // 조건 : 내가 작성한 후기 수 < 이 상품을 주문한 횟수
    private fun onClickWriteButton() {

        productDocId?.let { productId ->
            val userId = (activity as HomeActivity).loginUserDocumentId

            // 비동기로 ViewModel에 조건 체크 요청
            viewModel.checkReviewButtonVisibility(productId, userId)

            // ViewModel의 결과를 관찰하여 버튼 가시성 설정
            viewModel.isReviewButtonVisible.observe(viewLifecycleOwner) { isVisible ->
                binding.buttonUserProductInfoReview.visibility = if (isVisible) View.VISIBLE else View.GONE
            }
        }

    }
}