package com.example.frume.fragment.user_fragment.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCategoryDetailBinding
import com.example.frume.databinding.ItemProductBinding
import com.example.frume.model.ProductModel
import com.example.frume.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserCategoryDetailFragment : Fragment() {
    private var _binding: FragmentUserCategoryDetailBinding? = null
    private val binding get() = _binding!!
    private val args: UserCategoryDetailFragmentArgs by navArgs()
    var recyclerViewListByCategoryList = mutableListOf<ProductModel>()

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
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
    }

    private fun setLayout() {
        // 툴바 설정(상단이름 + 뒤로가기 구현)
        settingToolbar()
        /* // RecyclerView 설정
         settingRecyclerView()*/
        onClickToolbar()
        // settingRecyclerView
        settingCategoryRecyclerView()
        // 리사이클러뷰 갱신
        refreshCategoryRecyclerView(args.categoryMethod.str)
        setupSortDropdown()
    }

    private fun settingToolbar() {
        // 과일 카테고리 이름을 가져와 툴바 바꾸기
        if (args.categoryMethod.str == "검색") {
            binding.toolbarUserCategoryDetail.title = args.searchMethod
        } else {
            binding.toolbarUserCategoryDetail.title = args.categoryMethod.str
        }
    }

    // sehoon 네비게이션 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserCategoryDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    /*   private fun settingRecyclerView() {
           binding.apply {
               recyclerViewUserCategoryDetail.adapter = ProductRecyclerViewAdapter(recyclerViewListByCategoryL) { product ->
                   val action = UserCategoryDetailFragmentDirections.actionUserCategoryDetailToUserProductInfo(product.productDocId)
                   findNavController().navigate(action)
               }
           }
       }*/

    // 메인 RecyclerView 구성 메서드
    fun settingCategoryRecyclerView() {
        binding.apply {
            recyclerViewUserCategoryDetail.adapter = CategoryRecyclerViewAdapter()

            // 2열로 구성
            recyclerViewUserCategoryDetail.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    // 판매량 오름차 정렬
    fun sortBySalesCount() {
        recyclerViewListByCategoryList.sortBy { it.productSalesCount }

        binding.recyclerViewUserCategoryDetail.adapter?.notifyDataSetChanged()
    }

    // 판매량 내림차 정렬
    fun sortDescendingBySalesCount() {
        recyclerViewListByCategoryList.sortByDescending { it.productSalesCount }

        binding.recyclerViewUserCategoryDetail.adapter?.notifyDataSetChanged()
    }

    // 가격 오름차 정렬
    fun sortByPrice() {
        recyclerViewListByCategoryList.sortBy { it.productPrice }

        binding.recyclerViewUserCategoryDetail.adapter?.notifyDataSetChanged()
    }

    // 가격 내림차 정렬
    fun sortDescendingByPrice() {
        recyclerViewListByCategoryList.sortByDescending { it.productPrice }

        binding.recyclerViewUserCategoryDetail.adapter?.notifyDataSetChanged()
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

        // autoCompleteOrderStateTextView 항목 선택 이벤트 리스너 설정
        autoCompleteTextViewUserCategoryDetailFilterText.setOnItemClickListener { parent, view, position, id ->
            val selectedProductSalesCountState = parent.getItemAtPosition(position).toString()
            // 선택된 항목 처리
            // 선택된 항목에 따라 메서드를 실행
            when (position) {
                0 -> {
                    // 판매량 적은순 처리
                    sortBySalesCount()
                    Toast.makeText(
                        requireContext(),
                        "선택된 상태: 판매량 적은순",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                1 -> {
                    // 판매량 많은순 처리
                    sortDescendingBySalesCount()
                    Toast.makeText(
                        requireContext(),
                        "선택된 상태: 판매량 많은순",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                2 -> {
                    // 가격 낮은순 처리
                    sortByPrice()
                    Toast.makeText(
                        requireContext(),
                        "선택된 상태: 가격 낮은순",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                3 -> {
                    // 가격 높은순 처리
                    sortDescendingByPrice()
                    Toast.makeText(
                        requireContext(),
                        "선택된 상태: 가격 높은순",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        "알 수 없는 상태",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


    // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    fun refreshCategoryRecyclerView(category: String) {
        // Log.d("test100", "UserProductShowListFragment : refreshMainRecyclerView")

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                //  mutableList<productModel> 가져온다
                ProductService.gettingProductByCategory(category)
            }
            recyclerViewListByCategoryList = work1.await()

            binding.recyclerViewUserCategoryDetail.adapter?.notifyDataSetChanged()

            recyclerViewListByCategoryList.forEach {
                // Log.d("test100", "${it.productName} ${it.productSalesCount}")
            }
        }
    }


    // CategoryRecyclerView의 어뎁터
    inner class CategoryRecyclerViewAdapter :
        RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>() {
        inner class CategoryViewHolder(val itemProductBinding: ItemProductBinding) :
            RecyclerView.ViewHolder(itemProductBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val itemProductBinding = DataBindingUtil.inflate<ItemProductBinding>(
                layoutInflater,
                R.layout.item_product,
                parent,
                false
            )
            val categoryViewHolder = CategoryViewHolder(itemProductBinding)

            itemProductBinding.root.setOnClickListener {
                val action = UserCategoryDetailFragmentDirections.actionUserCategoryDetailToUserProductInfo(recyclerViewListByCategoryList[categoryViewHolder.adapterPosition].productDocId)
                findNavController().navigate(action)
            }

            return categoryViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewListByCategoryList.size
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.itemProductBinding.textViewItemProductTitle.text =
                recyclerViewListByCategoryList[position].productName
            // Log.d("test100", "recyclerViewListByCategoryList[${position}].productName : ${recyclerViewListByCategoryList[position].productName}")
            holder.itemProductBinding.textViewItemProductDescription.text =
                recyclerViewListByCategoryList[position].productDescription
        }
    }


   /* // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView(){
        Log.d("test100","UserProductShowListFragment : refreshMainRecyclerView")

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                //  mutableList<productModel> 가져온다
                ProductService.gettingProductByCategory("딸기")
            }
            recyclerViewListByCategory = work1.await()
           // settingRecyclerView(recyclerViewListByCategory)
            Log.d("test 100","recyclerViewListByCategory : ${recyclerViewListByCategory}")
        }
    }*/

}


/*
class ProductRecyclerViewAdapter(
    private val productList: List<ProductModel>, // 데이터 리스트
    private val onItemClick: (ProductModel) -> Unit // 클릭 리스너
) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>() {

    // ViewHolder 클래스
    inner class ProductViewHolder(val itemProductBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(itemProductBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // DataBinding 초기화
        val binding = DataBindingUtil.inflate<ItemProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_product,
            parent,
            false
        )
        val viewHolder = ProductViewHolder(binding)

        // 클릭 리스너 설정
        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(productList[position])
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.itemProductBinding.apply {
            textViewItemProductTitle.text = product.productName
            textViewItemProductDescription.text = product.productDescription
            */
/*imageViewItemProductThumbNail.setImageResource(product.productImgResourceId)*//*

        }
    }

    override fun getItemCount(): Int = productList.size
}*/