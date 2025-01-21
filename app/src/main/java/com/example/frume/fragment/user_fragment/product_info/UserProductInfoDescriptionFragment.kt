package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data.TempProduct
import com.example.frume.data_hj.DummyData
import com.example.frume.databinding.FragmentUserProductInfoDescriptionBinding
import com.example.frume.databinding.ItemProductInfoImageBinding
import com.example.frume.databinding.ItemProductInfoImageCarouselBinding
import com.example.frume.fragment.home_fragment.user_home.ProductItemClickListener
import com.example.frume.util.ItemMarginDecoration
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper


class UserProductInfoDescriptionFragment : Fragment() {
    private var _binding: FragmentUserProductInfoDescriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductImgAdapter

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
    }

    // 구매버튼 리스너
    private fun onClickBuyBtn() {
        binding.buttonUserProductInfoDescriptionBuy.setOnClickListener {
            val productName = binding.textViewUserProductInfoDescriptionName.text.toString()
            val productPrice = binding.textViewUserProductInfoDescriptionPrice.text.toString()
            val productImg = Storage.imgList[0].imgResourceId
            val numberOnly = productPrice.filter { it.isDigit() }.toInt()

            val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserProductInfoDialog(productName, numberOnly, productImg)
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
        private const val ARG_NUMBER = "arg_number"
        fun newInstance(): UserProductInfoDescriptionFragment {
            return UserProductInfoDescriptionFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    // putInt(ARG_NUMBER, number)

                }
            }
        }
    }
}