package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.data.TempReview
import com.example.frume.data.Storage
import com.example.frume.databinding.FragmentUserProductInfoReviewBinding
import com.example.frume.fragment.user_fragment.product_info.UserProductInfoDetailFragment.Companion


class UserProductInfoReviewFragment : Fragment(), ReviewClickListener {
    private var _binding: FragmentUserProductInfoReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductReviewAdapter
    private var currentPage = 0
    private val allReviews = Storage.reviewList // 전체 리뷰 데이터
    private val currentReviews = mutableListOf<TempReview>() // 현재 표시 중인 데이터
    private val pageSize = 10 // 한 페이지당 표시할 항목의 수
    private var productDocId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productDocId = it.getString(ARG_PRODUCT_DOC_ID)
        }
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
        // 어뎁터 설정
        settingRecyclerView()

        // 리뷰쓰기 화면전환 버튼
        onClickButtonWriteReview()

        onClickPageBtn()
    }

    // sehoon 리뷰작성 화면 이동 메서드
    private fun onClickButtonWriteReview() {
        binding.buttonUserProductInfoReview.setOnClickListener {
            val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserProductInfoWriteReview()
            findNavController().navigate(action)
        }
    }

    // sehoon RecyclerView를 구성하는 메서드
    private fun settingRecyclerView() {
        adapter = ProductReviewAdapter(currentReviews.toMutableList(), this)
        binding.recyclerViewUserProductInfoReview.adapter = adapter

        loadPage(0) // 첫 번째 페이지
        val maxPage = (allReviews.size + pageSize - 1) / pageSize
        binding.textViewUserProductInfoReviewMaxPageNumber.text = "$maxPage"
    }

    // sehoon 리싸이클러뷰 10개 페이지 로드
    private fun loadPage(page: Int) {
        val start = page * pageSize
        val end = (start + pageSize).coerceAtMost(allReviews.size)

        if (start < end) {
            val newItems = allReviews.subList(start, end)
            currentReviews.clear() // 현재 데이터 비우기
            currentReviews.addAll(allReviews.subList(start, end)) // 새 데이터 추가
            adapter.setItems(newItems)
            Log.d("newItems", currentReviews.toString())
            currentPage = page // 현재 페이지 업데이트
            binding.textViewUserProductInfoReviewNowPageNumber.text = "${currentPage + 1}" // 현재 페이지 UI 갱신

            // RecyclerView 맨 위로 스크롤
            binding.recyclerViewUserProductInfoReview.scrollToPosition(0)
        } else {
            // RecyclerView 맨 위로 스크롤
            Toast.makeText(requireContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // sehoon 이미지뷰 클릭 메서드
    private fun onClickPageBtn() {
        // "다음 페이지" 버튼
        binding.imageViewUserProductInfoReviewForward.setOnClickListener {
            if ((currentPage + 1) * pageSize < allReviews.size) {
                loadPage(currentPage + 1)
            } else {
                Toast.makeText(requireContext(), "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // "이전 페이지" 버튼
        binding.imageViewUserProductInfoReviewBack.setOnClickListener {
            if (currentPage > 0) {
                loadPage(currentPage - 1)
            } else {
                Toast.makeText(requireContext(), "첫 번째 페이지입니다.", Toast.LENGTH_SHORT).show()
            }
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

    override fun onClickProductItem(tempReview: TempReview) {
        Toast.makeText(requireContext(), tempReview.reviewTitle, Toast.LENGTH_SHORT).show()
    }
}