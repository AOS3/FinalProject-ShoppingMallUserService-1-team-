package com.example.frume.fragment.home.home_tab_second_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.frume.data.Product
import com.example.frume.databinding.FragmentUserHomeTabSecondBinding
import com.example.frume.factory.HomeSecondViewModelFactory
import com.example.frume.fragment.adapter.HomeProductAdapter
import com.example.frume.fragment.home.home_screen.UserHomeFragmentDirections
import com.example.frume.fragment.listener.ProductItemClickListener
import com.example.frume.repository.ProductRepository
import com.example.frume.util.CategoryType


class UserHomeTabSecondFragment : Fragment(), ProductItemClickListener {
    private var _binding: FragmentUserHomeTabSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeSecondViewModel by lazy {
        val repository = ProductRepository()
        val factory = HomeSecondViewModelFactory(repository)
        ViewModelProvider(this, factory)[HomeSecondViewModel::class.java]
    }
    private lateinit var adapter: HomeProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserHomeTabSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        observeDate()

    }

    private fun observeDate() {
        val number = arguments?.getInt(ARG_NUMBER)
        val category = number?.let { CategoryType.fromNumber(it) }

        if (number == 3) {
            viewModel.loadBestProduct() // 먼저 베스트 상품 데이터 로드
        } else {
            category?.let { viewModel.loadProduct(it) } // 나머지 카테고리 로드
        }

        viewModel.items.observe(viewLifecycleOwner) { products ->
            adapter = HomeProductAdapter(products.toMutableList(), this)
            adapter.add(products.toMutableList())
            binding.recyclerViewUserHomeTabSecondProductList.adapter = adapter
        }
    }


    companion object {
        private const val ARG_NUMBER = "arg_number"
        fun newInstance(number: Int): UserHomeTabSecondFragment {

            return UserHomeTabSecondFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    putInt(ARG_NUMBER, number)

                }
            }
        }
    }

    override fun onClickProductItem(product: Product) {
        val action = UserHomeFragmentDirections.actionNavigationHomeToUserProductInfo(product.productDocId)
        findNavController().navigate(action)
    }

}