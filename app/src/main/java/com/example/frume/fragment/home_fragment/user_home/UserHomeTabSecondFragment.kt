package com.example.frume.fragment.home_fragment.user_home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.frume.data.Storage
import com.example.frume.databinding.FragmentUserHomeTabSecondBinding
import com.example.frume.model.ProductModel
import com.example.frume.service.ProductService
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserHomeTabSecondFragment : Fragment(), ProductItemClickListener {
    private var _binding: FragmentUserHomeTabSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UserHomeViewModel>()
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
        setLayout()

        // 카테고리 번호 가져오기
        val categoryNumber = arguments?.getInt(ARG_NUMBER) ?: 0
        Log.d("test111", "전달된 카테고리 번호 : $categoryNumber") // 전달된 카테고리 번호 확인

        // ViewModel로 카테고리 번호 전달
        viewModel.updateData(categoryNumber)

        // ViewModel의 데이터 관찰하여 RecyclerView 업데이트
        viewModel.products.observe(viewLifecycleOwner) { products ->
            Log.d("test111", "관찰된 데이터 확인 : $products")
            adapter.add(products.toMutableList())
        }
    }




    private fun moveToProductInfo(product: ProductModel) {
        val action = UserHomeFragmentDirections.actionNavigationHomeToUserProductInfo(product.productName)
        findNavController().navigate(action)
    }

    private fun setLayout() {
        adapter = HomeProductAdapter(mutableListOf(), this)
        binding.recyclerViewUserHomeTabSecondProductList.adapter = adapter
    }

    override fun onClickProductItem(product: ProductModel) {
        Toast.makeText(requireContext(), product.productName, Toast.LENGTH_SHORT).show()
        moveToProductInfo(product)
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

}