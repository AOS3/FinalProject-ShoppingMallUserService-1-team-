package com.example.frume.fragment.user_fragment.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCategoryBinding
import com.example.frume.fragment.home_fragment.my_info.UserInfoFragmentDirections
import com.example.frume.fragment.home_fragment.user_home.UserHomeFragmentDirections
import com.example.frume.util.CategoryType
import com.example.frume.util.ProductCategoryDetailType

class UserCategoryFragment() : Fragment() {
    private var _binding: FragmentUserCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_category, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 클릭 리스너. 과일 이름을 클릭하면 넘어가도록한다.
        onClickCategory()
    }

    // 과일 선택
    private fun onClickCategory() {
        binding.textViewUserCategoryStrawberry.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_STRAWBERRY) }

        binding.textViewUserCategoryApple.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_APPLE) }

        binding.textViewUserCategoryTangerine.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_TANGERINE) }

        binding.textViewUserCategoryGrape.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_GRAPE) }

        binding.textViewUserCategoryMango.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_MANGO) }

        binding.textViewUserCategoryBlueberry.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_BLUEBERRY) }

        binding.textViewUserCategoryKiwi.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_KIWI) }

        binding.textViewUserCategoryOrange.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_ORANGE) }

        binding.textViewUserCategorySingle.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_SINGLE) }

        binding.textViewUserCategoryBulk.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_BULK) }

        binding.textViewUserCategoryPackage.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_PACKAGE) }

        binding.textViewUserCategorySale.setOnClickListener { navigateToCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_SALE) }
    }

    // sehoon 카테고리 클릭 메서드
    private fun navigateToCategoryDetail(category: ProductCategoryDetailType) {
        val action = UserCategoryFragmentDirections.actionNavigationCategoryToUserCategoryDetailFragment(category)
        findNavController().navigate(action)
    }
}