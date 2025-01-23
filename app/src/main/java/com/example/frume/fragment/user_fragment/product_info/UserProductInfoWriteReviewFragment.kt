package com.example.frume.fragment.user_fragment.product_info

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoWriteReviewBinding


class UserProductInfoWriteReviewFragment : Fragment(), WriteReviewClickListener {
    private var _binding: FragmentUserProductInfoWriteReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMultipleMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private val imageList = mutableListOf<Uri>()
    private lateinit var adapter: WriteReviewAdapter

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
        // Media Picker 초기화
        initPickMultipleMediaLauncher()
        // Layout 설정
        setLayout()
    }

    private fun setLayout() {
        onClickButtonReviewSubmit()
        onClickNavigationIconBackStack()
        onClickAddImage()
        settingRecyclerView(imageList)
        checkImage()
        //launchMediaPicker()
    }

    private fun checkImage() {
        binding.groupImageYes.visibility = View.VISIBLE
        binding.groupImageNo.visibility = View.INVISIBLE
//        if (imageList.isEmpty()) {
//            binding.groupImageYes.visibility = View.INVISIBLE
//            binding.groupImageNo.visibility = View.VISIBLE
//        } else {
//            binding.groupImageYes.visibility = View.VISIBLE
//            binding.groupImageNo.visibility = View.INVISIBLE
//        }
    }

    private fun moveToProductInfo() {
        findNavController().navigateUp()
    }

    private fun settingRecyclerView(imageList: MutableList<Uri>) {
        adapter = WriteReviewAdapter(imageList, this)
        binding.recyclerViewUserProductInfoWriteReview.adapter

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

    private fun initPickMultipleMediaLauncher(pos: Int = 0) {
        pickMultipleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
                adapter.addImage(uris, pos)

                Log.d("TEST2",imageList.toString())
                checkImage()
            }
    }

    private fun onClickAddImage() {
        binding.imageViewAddBtn.setOnClickListener { launchMediaPicker() }
        binding.imageViewUserProductInfoWriteReviewAddImage.setOnClickListener { launchMediaPicker() }
    }

    private fun launchMediaPicker() {
        pickMultipleMediaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
        )
    }

    override fun onClickReviewImg(pos: Int) {
        launchMediaPicker()
    }

    override fun onClickRemoveBtn(pos: Int) {

    }
}