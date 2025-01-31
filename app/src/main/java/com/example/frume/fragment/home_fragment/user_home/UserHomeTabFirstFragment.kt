package com.example.frume.fragment.home_fragment.user_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data.TempProduct
import com.example.frume.databinding.FragmentUserHomeTabFirstBinding
import com.example.frume.service.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class UserHomeTabFirstFragment : Fragment(), ProductItemClickListener {

    private var _binding: FragmentUserHomeTabFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home_tab_first, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // View가 사라질 때 binding을 해제하여 메모리 누수 방지
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        setRecyclerView()
        setBanner()
    }

    private fun setRecyclerView() {
        lifecycleScope.launch {
            val products = withContext(Dispatchers.IO) {
                ProductService.gettingProductAll().map { productModel ->
                    TempProduct(
                        productDocId = productModel.productDocId,
                        productImgResourceId = R.drawable.btn_background,
                        productName = productModel.productName,
                        productPrice = productModel.productPrice,
                        productDescription = productModel.productDescription,
                        productCategory = productModel.productHomeCategory
                    )
                }
            }

            if (isAdded && _binding != null) { // Fragment가 활성 상태인지 확인 후 binding 사용
                binding?.let { safeBinding ->
                    val adapter = HomeProductAdapter(products.toMutableList(), this@UserHomeTabFirstFragment)
                    safeBinding.recyclerView.adapter = adapter
                }
            }
        }
    }

    private fun setBanner() {
        val dummyList = Storage.bannerList
        val initialPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % dummyList.size)

        binding?.let { safeBinding ->
            safeBinding.viewPager.adapter = HomeBannerAdapter(dummyList)
            safeBinding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            safeBinding.viewPager.setCurrentItem(initialPosition, false)
            safeBinding.textViewBannerLastNumber.text = "${dummyList.size}"
        }

        binding?.viewPager?.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val currentPage = position % dummyList.size + 1
                    binding?.textViewBannerFirstNumber?.text = "$currentPage"
                }
            })
        }
    }

    override fun onClickProductItem(product: TempProduct) {
        Toast.makeText(requireContext(), product.productName, Toast.LENGTH_SHORT).show()
        val action = UserHomeFragmentDirections.actionNavigationHomeToUserProductInfo(product.productDocId)
        findNavController().navigate(action)
    }

    companion object {
        fun newInstance(): UserHomeTabFirstFragment {
            return UserHomeTabFirstFragment()
        }
    }
}