package com.example.frume.fragment.user_fragment.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data.TempProduct
import com.example.frume.databinding.FragmentUserCategoryDetailBinding
import com.example.frume.databinding.ItemProductBinding
import com.example.frume.model.ProductModel
import com.example.frume.service.ProductService
import com.example.frume.util.ProductInfoType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext



class UserCategoryDetailFragment : Fragment() {
    private var _binding: FragmentUserCategoryDetailBinding? = null
    private val binding get() = _binding!!
    private val args: UserCategoryDetailFragmentArgs by navArgs()
    var recyclerViewListByCategory = mutableListOf<ProductModel>()

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
        //settingRecyclerView() : Storage 사용 -> 일단 주석 처리함
        onClickToolbar()
        // 리사이클러뷰 갱신
        refreshMainRecyclerView()
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

    // Storage 사용 -> 일단 주석 처리함
//    private fun settingRecyclerView() {
//        binding.apply {
//            val productTempList = Storage.productList
//            recyclerViewUserCategoryDetail.adapter = ProductRecyclerViewAdapter(productTempList) { product ->
//                val action = UserCategoryDetailFragmentDirections.actionUserCategoryDetailToUserProductInfo(product)
//                findNavController().navigate(action)
//            }
//        }
//    }

    // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView(){
        Log.d("test100","UserProductShowListFragment : refreshMainRecyclerView")

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                //  mutableList<productModel> 가져온다
                ProductService.gettingProductByCategory("딸기")
            }
            recyclerViewListByCategory = work1.await()

            Log.d("test 100","recyclerViewListByCategory : ${recyclerViewListByCategory}")
        }
    }


}

class ProductRecyclerViewAdapter(
    private val productList: List<TempProduct>, // 데이터 리스트
    private val onItemClick: (TempProduct) -> Unit // 클릭 리스너
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
            imageViewItemProductThumbNail.setImageResource(product.productImgResourceId)
        }
    }

    override fun getItemCount(): Int = productList.size
}