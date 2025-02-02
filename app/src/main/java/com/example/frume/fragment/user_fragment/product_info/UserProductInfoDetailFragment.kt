package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoDetailBinding
import com.example.frume.factory.ProductDetailModelFactory
import com.example.frume.repository.ProductRepository


class UserProductInfoDetailFragment : Fragment() {
    private var _binding: FragmentUserProductInfoDetailBinding? = null
    private val binding get() = _binding!!
    private var productDocId: String? = null
    private val viewModel: ProductDetailViewModel by lazy {
        val repository = ProductRepository()
        val factory = ProductDetailModelFactory(repository)
        ViewModelProvider(this, factory)[ProductDetailViewModel::class.java]
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_detail, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel.loadProduct(productDocId!!)
        viewModel.items.observe(viewLifecycleOwner) {
            binding.textViewProductInfoDetailBreed.text = it.productCategory2
            binding.textViewProductInfoDetailWeight.text = "${it.productVolume}${it.productUnit}"
        }
    }


    companion object {
        private const val ARG_PRODUCT_DOC_ID = "product_doc_id"
        fun newInstance(productDocId: String): UserProductInfoDetailFragment {
            return UserProductInfoDetailFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_DOC_ID, productDocId)
                }
            }
        }
    }
}