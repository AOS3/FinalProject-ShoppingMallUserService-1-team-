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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.databinding.FragmentUserProductInfoDescriptionBinding
import com.example.frume.databinding.ItemProductInfoImageCarouselBinding
import com.example.frume.factory.ProductInfoModelFactory
import com.example.frume.model.ProductModel
import com.example.frume.repository.ProductRepository
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
    private lateinit var adapter2: ProductCarouselAdapter

    private var productDocId: String? = null
    private val viewModel: ProductInfoViewModel by lazy {
        val repository = ProductRepository()
        val factory = ProductInfoModelFactory(repository)
        ViewModelProvider(this, factory)[ProductInfoViewModel::class.java]
    }

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_description, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setLayout()
    }


    private fun setLayout() {
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewUserProductInfoDescriptionCarouselImage)
        observeData()
        onClickBuyBtn()
    }

    // 구매버튼 리스너
    private fun onClickBuyBtn() {
        binding.buttonUserProductInfoDescriptionBuy.setOnClickListener {
            val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserProductInfoDialog(productDocId!!)
            findNavController().navigate(action)
        }
    }


    private fun observeData() {
        viewModel.loadProduct(productDocId!!)
        viewModel.items.observe(viewLifecycleOwner) {
            adapter = ProductImgAdapter(it.productImages)
            binding.recyclerViewUserProductInfoDescriptionDescriptionImage.adapter = adapter
            binding.textViewUserProductInfoDescriptionName.text = it.productName
            binding.textViewUserProductInfoDescriptionPrice.text = it.productPrice.convertThreeDigitComma()
            binding.textViewUserProductInfoDescription.text = it.productDescription

            adapter2 = ProductCarouselAdapter(it.productImages)
            binding.recyclerViewUserProductInfoDescriptionCarouselImage.adapter = adapter2
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