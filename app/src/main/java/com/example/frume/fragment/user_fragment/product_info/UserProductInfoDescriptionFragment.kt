package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data_hj.DummyData
import com.example.frume.databinding.FragmentUserProductInfoDescriptionBinding
import com.example.frume.databinding.ItemProductInfoImageCarouselBinding
import com.example.frume.service.ProductService
import com.example.frume.util.convertThreeDigitComma
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserProductInfoDescriptionFragment : Fragment() {
    private var _binding: FragmentUserProductInfoDescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductImgAdapter
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

        // Inflate the layout for this fragment

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_description, container, false)

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
        settingRecyclerViewCarousel()
        settingRecyclerViewImage()
        // recyclerViewImage 실행
        onClickBuyBtn()
        getProductData()
    }

    private fun getProductData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val productList = ProductService.getProductInfo(productDocId!!)
                withContext(Dispatchers.Main) {
                    // UI
                    productList.forEach { item ->
                        with(binding) {
                            textViewUserProductInfoDescriptionName.text = item.productName
                            textViewUserProductInfoDescription.text = item.productDescription
                            textViewUserProductInfoDescriptionPrice.text = item.productPrice.convertThreeDigitComma()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "no data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 구매버튼 리스너
    private fun onClickBuyBtn() {
        binding.buttonUserProductInfoDescriptionBuy.setOnClickListener {
            val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserProductInfoDialog(productDocId!!)
            findNavController().navigate(action)
        }
    }


    // RecyclerView를 구성하는 메서드
    private fun settingRecyclerViewImage() {
        val tempImgList = Storage.imgList
        adapter = ProductImgAdapter(tempImgList.toMutableList())
        binding.recyclerViewUserProductInfoDescriptionDescriptionImage.adapter = adapter

    }


    // 카로셀 어뎁터 적용
    // carousel 실행
    private fun settingRecyclerViewCarousel() {
        binding.apply {
            recyclerViewUserProductInfoDescriptionCarouselImage.adapter = RecyclerViewCarouselAdapter()
            // 회전 목마용 LayoutManager
            recyclerViewUserProductInfoDescriptionCarouselImage.layoutManager =
                CarouselLayoutManager()

            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(recyclerViewUserProductInfoDescriptionCarouselImage)
        }
    }

    // 카로셀 어뎁터 클래스
    inner class RecyclerViewCarouselAdapter : RecyclerView.Adapter<RecyclerViewCarouselAdapter.CarouselViewHolder>() {
        inner class CarouselViewHolder(val itemProductInfoImageCarouselBinding: ItemProductInfoImageCarouselBinding) :
            RecyclerView.ViewHolder(itemProductInfoImageCarouselBinding.root),
            OnClickListener {
            override fun onClick(v: View?) {
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
            val itemProductInfoImageCarouselBinding =
                ItemProductInfoImageCarouselBinding.inflate(layoutInflater, parent, false)
            val carouselViewHolder = CarouselViewHolder(itemProductInfoImageCarouselBinding)
            itemProductInfoImageCarouselBinding.root.setOnClickListener(carouselViewHolder)
            return carouselViewHolder
        }

        override fun getItemCount(): Int {
            return DummyData.dummyImages.size
            Log.d("test100", "imageSize : ${DummyData.dummyImages.size}")
        }

        override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
            holder.itemProductInfoImageCarouselBinding.imageViewItemProductInfoImageCarousel.setImageResource(
                DummyData.dummyImages[position]
            )
        }
    }

    companion object {
        private const val ARG_PRODUCT_DOC_ID = "product_doc_id"

        fun newInstance(productDocId: String): UserProductInfoDescriptionFragment {
            return UserProductInfoDescriptionFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_DOC_ID, productDocId)
                }
            }
        }
    }
}