package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoWriteReviewBinding
import com.example.frume.fragment.user_fragment.product_info.UserProductInfoDetailFragment.Companion
import com.example.frume.home.HomeActivity
import com.example.frume.model.ReviewModel
import com.example.frume.service.ReviewService
import com.example.frume.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserProductInfoWriteReviewFragment : Fragment(), WriteReviewClickListener {
    private var _binding: FragmentUserProductInfoWriteReviewBinding? = null
    private val binding get() = _binding!!
    private var productDocId: String? = null

    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var changeMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var selectedPosition: Int = -1

    private val adapter: WriteReviewAdapter by lazy { WriteReviewAdapter(this) }
    private var imageCount = 0
    private val args: UserProductInfoWriteReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_product_info_write_review,
            container,
            false
        )
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

    private fun setLayout() {
        initChangeMediaLauncher()
        initPickMultipleMediaLauncher()
        onClickButtonReviewSubmit()
        onClickNavigationIconBackStack()
        onClickAddImage()
        settingRecyclerView()
        onClickConfirm()
        setImageCount()
    }

    private fun moveToProductInfo() {
        findNavController().navigateUp()
    }

    private fun settingRecyclerView() {
        binding.recyclerViewUserProductInfoWriteReview.adapter = adapter
    }

    private fun onClickButtonReviewSubmit() {
        binding.buttonUserProductInfoReviewConfirm.setOnClickListener {
            moveToProductInfo()
        }
    }

    private fun onClickNavigationIconBackStack() {
        binding.toolbarUserProductInfoWriteReview.setNavigationOnClickListener {
            moveToProductInfo()
        }
    }

    // 기존 initPickMediaLauncher 호출 전에 새로운 changeMediaLauncher 초기화
    private fun initChangeMediaLauncher() {
        changeMediaLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null && selectedPosition != -1) {
                adapter.changeImage(uri, selectedPosition)
                setImageCount()
            }
        }
    }

    private fun initPickMultipleMediaLauncher() {
        Log.d("TEST", imageCount.toString())
        imageCount = adapter.itemCount
        val pickImgCount = 3 - imageCount
        Log.d("PICKIMGCOUNT", pickImgCount.toString())
        pickMediaLauncher = registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia()
        ) { uris ->
            if (uris.isNotEmpty()) {
                val a = 3 - imageCount
                val imaToAd = uris.take(a)
                adapter.addImage(imaToAd, imageCount)
                setImageCount()
            }
        }
    }

    private fun setImageCount() {
        imageCount = adapter.itemCount
        binding.textViewUserProductInfoWriteReviewImageCount.text = "($imageCount / 3)"

    }

    private fun onClickAddImage() {
        binding.cardViewUserProductInfoWriteReviewAddImage.setOnClickListener {
            if (adapter.itemCount == 3) {
                Toast.makeText(requireContext(), "사진은 최대 3장 첨부 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                launchMediaPicker()
            }
        }
    }

    private fun onClickConfirm() {
        binding.buttonUserProductInfoReviewConfirm.setOnClickListener {
            val reviewTitle = binding.editTextUserProductInfoWriteReviewTitle.editText?.text.toString()
            val imageList = adapter.getImage()
            val imageStringList = imageList.map { it.reviewImage.orEmpty() }.toMutableList()

            val ratingPoint = binding.ratingBarUserProductInfoWriteReview.rating
            val reviewContent = binding.editTextUserProductInfoWriteReview.editText?.text.toString()
            val userDocId = activity as HomeActivity
            val productDocId = args.productDocId
            if (reviewTitle.isEmpty() || imageList.isEmpty() || reviewContent.isEmpty()) {
                Toast.makeText(requireContext(), "빈칸을 모두 기입해주세요", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    Log.d("ratingPoint", "$ratingPoint")

                    val reviewModel = ReviewModel().apply {
                        this.reviewCustomerDocId = userDocId.loginUserDocumentId
                        this.reviewTitle = reviewTitle
                        this.reviewProductDocId = productDocId
                        this.reviewImagesPath = imageStringList
                        this.reviewRatingPoint = ratingPoint
                        this.reviewContent = reviewContent
                    }
                    ReviewService.setUserReview(reviewModel)

                    withContext(Dispatchers.Main) {
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun launchMediaPicker() {
        pickMediaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
        )
    }

    override fun onClickReviewImg(pos: Int) {
        selectedPosition = pos // 포지션값 저장
        changeMediaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    override fun onClickRemoveBtn(pos: Int) {
        adapter.removeImage(pos)
        setImageCount()
    }
}