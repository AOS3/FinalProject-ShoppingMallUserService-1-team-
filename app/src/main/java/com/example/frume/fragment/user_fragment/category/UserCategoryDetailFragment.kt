package com.example.frume.fragment.user_fragment.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCategoryDetailBinding
import com.example.frume.databinding.ItemProductBinding
import com.example.frume.util.ProductInfoType


class UserCategoryDetailFragment : Fragment() {
    private var _binding: FragmentUserCategoryDetailBinding? = null
    private val binding get() = _binding!!
    private val args: UserCategoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_category_detail, container, false)

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
        // RecyclerView 설정
        settingRecyclerView()
        onClickToolbar()
    }

    fun settingToolbar() {
        // 과일 카테고리 이름을 가져와 툴바 바꾸기
        val selectedFruit = arguments?.getString("selectedFruit") ?: "알 수 없는 과일"
        binding.toolbarUserCategoryDetail.title = args.categoryMethod.str
    }

    // sehoon 네비게이션 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserCategoryDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun settingRecyclerView() {
        binding.apply {
            recyclerViewUserCategoryDetail.adapter = ProductRecyclerViewAdapter(createDummyData()) { product ->
                openProductInfoFragment(product)
            }

            // recyclerViewUserCategoryDetail.layoutManager = GridLayoutManager(context, 2) // 2열 그리드
            // val divider = MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL)
            // recyclerViewUserCategoryDetail.addItemDecoration(divider)
        }
    }

    private fun createDummyData(): List<Product> {
        return listOf(
            Product(1, "딸기", "유기농으로 키워 신선합니다.", R.mipmap.ic_launcher),
            Product(2, "사과", "달콤한 사과입니다.", R.mipmap.ic_launcher),
            Product(3, "포도", "씨 없는 달콤한 포도입니다.", R.mipmap.ic_launcher),
            Product(4, "망고", "열대과일의 여왕, 신선한 망고입니다.", R.mipmap.ic_launcher),
            Product(5, "블루베리", "항산화 성분이 가득한 블루베리.", R.mipmap.ic_launcher)
        )
    }

    private fun openProductInfoFragment(product: Product) {

    }

}

// 데이터 클래스 정의
data class Product(val id: Int, val title: String, val description: String, val imageResId: Int)

class ProductRecyclerViewAdapter(
    private val productList: List<Product>, // 데이터 리스트
    private val onItemClick: (Product) -> Unit // 클릭 리스너
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
            textViewItemProductTitle.text = product.title
            textViewItemProductDescription.text = product.description
            imageViewItemProductThumbNail.setImageResource(product.imageResId)
        }
    }

    override fun getItemCount(): Int = productList.size
}