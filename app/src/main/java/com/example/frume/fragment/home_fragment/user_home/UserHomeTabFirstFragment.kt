package com.example.frume.fragment.home_fragment.user_home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.frume.R
import com.example.frume.data.Product
import com.example.frume.data.Storage
import com.example.frume.databinding.FragmentUserHomeTabFirstBinding
import com.example.frume.factory.HomeViewModelFactory
import com.example.frume.repository.ProductRepository


class UserHomeTabFirstFragment : Fragment(), ProductItemClickListener {
    private var _binding: FragmentUserHomeTabFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by lazy {
        val repository = ProductRepository()
        val factory = HomeViewModelFactory(repository)
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }
    private lateinit var adapter: HomeProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_home_tab_first,
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
        binding.lifecycleOwner = viewLifecycleOwner
        setLayout()

    }

    private fun setLayout() {
        setBanner()
        observeData()
    }

    private fun observeData() {
        viewModel.loadProduct()
        viewModel.items.observe(viewLifecycleOwner) {
            adapter = HomeProductAdapter(it.toMutableList(), this)
            binding.recyclerView.adapter = adapter
        }
    }


    // 배너 설정
    private fun setBanner() {
        val dummyList = Storage.bannerList
        val initialPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % dummyList.size)

        with(binding) {
            viewPager.adapter = HomeBannerAdapter(dummyList)
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.setCurrentItem(initialPosition, false)
            textViewBannerLastNumber.text = "${dummyList.size}"
        }

        // 현재 배너 페이지 숫자를 보여줌
        binding.viewPager.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val currentPage = position % dummyList.size + 1
                    binding.textViewBannerFirstNumber.text = "$currentPage"
                }
            })
        }
    }

    companion object {
        fun newInstance(): UserHomeTabFirstFragment {
            return UserHomeTabFirstFragment().apply {
            }
        }
    }

    override fun onClickProductItem(product: Product) {
        val action = UserHomeFragmentDirections.actionNavigationHomeToUserProductInfo(product.productDocId)
        findNavController().navigate(action)
    }
}