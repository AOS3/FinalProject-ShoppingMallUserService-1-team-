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
import com.example.frume.data.TempProduct
import com.example.frume.databinding.FragmentUserHomeTabSecondBinding


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

    // tabLayout 연결
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        val number = arguments?.getInt(ARG_NUMBER) ?: 0
        val products = Storage.productList.filter {
            it.productCategory.number == number
        }

        adapter.add(products.toMutableList())
//        viewModel.updateData(number)
//        Log.d("number", number.toString())
//        viewModel.product.observe(viewLifecycleOwner) {
//            adapter.add(it.toMutableList())
//        }

    }

    private fun moveToProductInfo(product: TempProduct) {
        val action = UserHomeFragmentDirections.actionNavigationHomeToUserProductInfo(product)
        findNavController().navigate(action)
    }

    private fun setLayout() {
        adapter = HomeProductAdapter(mutableListOf(), this)
        binding.recyclerViewUserHomeTabSecondProductList.adapter = adapter
    }

    override fun onClickProductItem(product: TempProduct) {
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