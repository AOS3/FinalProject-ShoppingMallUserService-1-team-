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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.data_hj.DummyData
import com.example.frume.databinding.FragmentUserProductInfoDescriptionBinding
import com.example.frume.databinding.FragmentUserProductInfoDetailBinding
import com.example.frume.databinding.ItemProductInfoImageBinding
import com.example.frume.databinding.ItemProductInfoImageCarouselBinding
import com.example.frume.util.ItemMarginDecoration
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper


class UserProductInfoDescriptionFragment() : Fragment() {
    private var _binding: FragmentUserProductInfoDescriptionBinding? = null
    private val binding get() = _binding!!

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
        onClickButtonPurches()
        // recyclerViewImage 실행
        settingRecyclerViewImage()
        onClickButtonPurches()
    }

    // 구매버튼 리스너
    private fun onClickButtonPurches() {
        binding.buttonUserProductInfoDescriptionBuy.setOnClickListener {
            val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserPaymentScreen()
            findNavController().navigate(action)

        }
    }


    // RecyclerView를 구성하는 메서드
    private fun settingRecyclerViewImage() {
        binding.apply {
            // 어뎁터
            recyclerViewUserProductInfoDescriptionDescriptionImage.adapter =
                RecyclerViewImageAdapter()
            // 구분선
            recyclerViewUserProductInfoDescriptionDescriptionImage.addItemDecoration(
                ItemMarginDecoration(26) // 상하 여백을 26dp로 설정
            )
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewImageAdapter :
        RecyclerView.Adapter<RecyclerViewImageAdapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(val itemProductInfoImageBinding: ItemProductInfoImageBinding) :
            RecyclerView.ViewHolder(itemProductInfoImageBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {

            val itemProductInfoImageBinding = ItemProductInfoImageBinding.inflate(layoutInflater, parent, false)

            val viewHolderImage = ViewHolderMain(itemProductInfoImageBinding)


            return viewHolderImage
        }

        override fun getItemCount(): Int {
            return DummyData.dummyImages.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.itemProductInfoImageBinding.imageViewItemProductInfoImage.setImageResource(DummyData.dummyImages[position])
        }
    }


    // 카로셀 어뎁터 적용
    // carousel 실행
    private fun settingRecyclerViewCarousel() {
        binding.apply {
            recyclerViewUserProductInfoDescriptionCarouselImage.adapter = RecyclerViewCarouselAdapter()
            // 회전 목마용 LayoutManager
            recyclerViewUserProductInfoDescriptionCarouselImage.layoutManager =
                CarouselLayoutManager()
            // recyclerView.layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
            // recyclerView.layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy())
            // recyclerView.layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy(), RecyclerView.VERTICAL)

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