package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data.TempReview
import com.example.frume.databinding.FragmentUserInfoReviewBinding
import com.example.frume.databinding.ItemDeliverySpotBinding
import com.example.frume.databinding.ItemReviewBinding
import com.example.frume.fragment.home_fragment.my_info.my_profile_setting.UserAddressManageFragmentDirections
import com.example.frume.fragment.user_fragment.product_info.ProductReviewAdapter
import com.example.frume.fragment.user_fragment.product_info.ReviewClickListener
import com.example.frume.home.HomeActivity
import com.example.frume.model.ReviewModel
import com.example.frume.service.ReviewService
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread


class UserInfoReviewFragment : Fragment(), ReviewClickListener {
    private var _binding: FragmentUserInfoReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductReviewAdapter
    lateinit var homeActivity: HomeActivity

    var reviewList = mutableListOf<ReviewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivity = activity as HomeActivity
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
        setLayout()
    }

    private fun settingToolbar() {
        binding.toolbarUserInfoReview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setLayout() {
        settingRecyclerViewUserReviewManager()
        settingToolbar()
        gettingReviewList(homeActivity.loginUserDocumentId)
    }

    // reviewList
    fun gettingReviewList(userDocId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                reviewList = ReviewService.gettingMyReviewList(userDocId)
            }
            work1.join()
        }
    }

    // recyclerView를 구성하는 메서드
    fun settingRecyclerViewUserReviewManager() {
        binding.apply {
            // RecyclerView의 어댑터에 리뷰 리스트 전달
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // 리뷰 정보가 null이 아니거나 값이 설정될 때까지 반복 (2초 제한)
                    withTimeout(2000) {  // 2000ms = 2초
                        while (reviewList.size == 0) {
                            delay(500)  // 0.5초마다 확인
                        }
                    }

                    // 어뎁터
                    recyclerViewUserInfoReview.adapter = RecyclerViewUserInfoReviewAdapter(reviewList)
                    // LayoutManager
                    recyclerViewUserInfoReview.layoutManager = LinearLayoutManager(homeActivity)
                    // 구분선
                    // val deco = MaterialDividerItemDecoration(homeActivity, MaterialDividerItemDecoration.VERTICAL)
                    // recyclerViewUserInfoReview.addItemDecoration(deco)

                } catch (e: TimeoutCancellationException) {
                    // 2초 이내에 리뷰 정보가 로드되지 않으면 타임아웃 처리
                    // 여기서 타임아웃 후 처리를 할 수 있음 (예: 로딩 실패 메시지 표시)
                    recyclerViewUserInfoReview.adapter = RecyclerViewUserInfoReviewAdapter(reviewList)
                }
            }
        }
    }

    // recyclerView 어댑터
    inner class RecyclerViewUserInfoReviewAdapter(private val reviewList: MutableList<ReviewModel>) :
        RecyclerView.Adapter<RecyclerViewUserInfoReviewAdapter.ViewHolderUserReview>() {

        inner class ViewHolderUserReview(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    // 클릭 시 동작
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserReview {
            val binding = ItemReviewBinding.inflate(layoutInflater, parent, false)
            return ViewHolderUserReview(binding)
        }

        override fun getItemCount(): Int {
            return reviewList.size
        }

        override fun onBindViewHolder(holder: ViewHolderUserReview, position: Int) {
            val item = reviewList[position]

            holder.binding.apply {
                ratingBarItemReview.rating = item.reviewRatingPoint
                textViewRecyclerViewReviewTitle.text = item.reviewTitle
                textViewRecyclerViewReviewText.text = item.reviewContent

                // 타임스탬프 포맷팅
                textViewRecyclerViewReviewDate.text = formatTimestamp(item.reviewTimeStamp)
            }
        }

        // 타임스탬프 포맷팅 함수
        private fun formatTimestamp(timestamp: Timestamp): String {
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp.seconds * 1000))  // Timestamp는 초 단위이므로 밀리초로 변환
        }
    }

    override fun onClickProductItem(reView: TempReview) {
        Toast.makeText(requireContext(), reView.reviewTitle, Toast.LENGTH_SHORT).show()
    }
}