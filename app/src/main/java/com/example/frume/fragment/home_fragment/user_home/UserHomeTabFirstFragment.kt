package com.example.frume.fragment.home_fragment.user_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.frume.R
import com.example.frume.data.TempProduct
import com.example.frume.databinding.FragmentUserHomeTabFirstBinding


class UserHomeTabFirstFragment : Fragment(), ProductItemClickListener {
    private var _binding: FragmentUserHomeTabFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UserHomeViewModel>()
    private lateinit var adapter: HomeProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home_tab_first, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        viewModel.product.observe(viewLifecycleOwner) {
            adapter.add(it.toMutableList())
        }
    }

    private fun setLayout() {
        adapter = HomeProductAdapter(mutableListOf(), this)
        binding.recyclerView.adapter = adapter
        setBanner()
    }

    // 배너 설정
    private fun setBanner() {
        val dummyList = viewModel.getBannerProduct()
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

    // 리싸이클러뷰 클릭 이벤트
    override fun onClickProductItem(product: TempProduct) {
        // 상세 정보로 이동
        Toast.makeText(requireContext(), product.productName, Toast.LENGTH_SHORT).show()
        // 보내 주고싶은 값을 파라미터로 전달
        val action = UserHomeFragmentDirections.actionNavigationHomeToUserProductInfo(product)
        findNavController().navigate(action)
    }

    companion object {
        fun newInstance(): UserHomeTabFirstFragment {
            return UserHomeTabFirstFragment().apply {
            }
        }
    }
}