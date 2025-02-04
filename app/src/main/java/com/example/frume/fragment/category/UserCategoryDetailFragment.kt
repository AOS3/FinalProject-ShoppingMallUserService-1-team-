package com.example.frume.fragment.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCategoryDetailBinding
import com.example.frume.adapter.CategoryAdapter
import com.example.frume.listener.ProductCategoryItemClickListener
import com.example.frume.model.ProductModel
import com.example.frume.service.ProductService
import com.example.frume.util.SortType
import com.example.frume.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserCategoryDetailFragment : Fragment(), ProductCategoryItemClickListener {
    private var _binding: FragmentUserCategoryDetailBinding? = null
    private val binding get() = _binding!!
    private val args: UserCategoryDetailFragmentArgs by navArgs()
    private lateinit var categoryAdapter: CategoryAdapter
    private val recyclerViewListByCategoryList = mutableListOf<ProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_category_detail,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewUserCategoryDetail.adapter = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
    }

    private fun setLayout() {
        // 툴바 설정(상단이름 + 뒤로가기 구현)
        settingToolbar()
        onClickToolbar()
        settingCategoryRecyclerView()
        setupSortDropdown()
    }

    private fun settingToolbar() {
        // 과일 카테고리 이름을 가져와 툴바 바꾸기
        if (args.categoryMethod.str == "검색") {
            binding.toolbarUserCategoryDetail.title = args.searchMethod
            Log.d("detail", "검색으로 진입")
            refreshCategoryRecyclerView(1)
        } else {
            binding.toolbarUserCategoryDetail.title = args.categoryMethod.str
            Log.d("detail", "카테고리로 진입")
            refreshCategoryRecyclerView(2)
        }
    }

    // sehoon 네비게이션 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserCategoryDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 메인 RecyclerView 구성 메서드
    fun settingCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter(this)
        binding.recyclerViewUserCategoryDetail.adapter = categoryAdapter
    }

    // 정렬 메서드
    private fun sortList(sortType: SortType) {
        val sortedList = when (sortType) {
            SortType.SALES_ASC -> recyclerViewListByCategoryList.sortedBy { it.productSalesCount }
            SortType.SALES_DESC -> recyclerViewListByCategoryList.sortedByDescending { it.productSalesCount }
            SortType.PRICE_ASC -> recyclerViewListByCategoryList.sortedBy { it.productPrice }
            SortType.PRICE_DESC -> recyclerViewListByCategoryList.sortedByDescending { it.productPrice }
        }

        // 정렬 후 리스트 갱신
        categoryAdapter.updateList(sortedList)
    }

    // 정렬 드롭다운 메뉴 버튼 리스너
    private fun setupSortDropdown() {
        val autoCompleteTextViewUserCategoryDetailFilterText =
            binding.autoCompleteTextViewUserCategoryDetailSalesCount
        // 드롭다운 데이터 정의
        val filterState = listOf("판매량 적은순", "판매량 많은순", "가격 낮은순", "가격 높은순")

        // ArrayAdapter 생성 (autoCompleteOrderStateTextView에 데이터를 연결)
        val adapterSalesCountState = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            filterState
        )

        // autoCompleteOrderStateTextView에 어댑터 연결
        autoCompleteTextViewUserCategoryDetailFilterText.setAdapter(adapterSalesCountState)

        // autoCompleteSearchPeriodTextView에 어댑터 연결

        autoCompleteTextViewUserCategoryDetailFilterText.setAdapter(adapterSalesCountState)

        autoCompleteTextViewUserCategoryDetailFilterText.setOnItemClickListener { parent, _, position, _ ->
            val selectedSortType = when (position) {
                0 -> SortType.SALES_ASC
                1 -> SortType.SALES_DESC
                2 -> SortType.PRICE_ASC
                3 -> SortType.PRICE_DESC
                else -> null
            }

            selectedSortType?.let { sortList(it) } ?: requireContext().showToast("검색 결과가 없습니다")
        }
    }

    // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    private fun refreshCategoryRecyclerView(category: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val resultList = withContext(Dispatchers.IO) {
                when (category) {
                    1 -> ProductService.getSearchProductInfo(args.searchMethod!!)
                    else -> ProductService.gettingProductByCategory(args.categoryMethod.str)
                }
            }

            if (resultList.isEmpty()) {
                binding.textViewUserCategoryDetailNoSearch.visibility = View.VISIBLE
            } else {
                binding.textViewUserCategoryDetailNoSearch.visibility = View.GONE
                recyclerViewListByCategoryList.clear()
                recyclerViewListByCategoryList.addAll(resultList)
            }
            // 어댑터 갱신
            categoryAdapter.updateList(recyclerViewListByCategoryList)
        }
    }

    override fun onClickProductItem(product: ProductModel) {
        val action = UserCategoryDetailFragmentDirections.actionUserCategoryDetailToUserProductInfo(product.productDocId)
        findNavController().navigate(action)
    }
}