package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.databinding.FragmentUserProductInfoBinding
import com.google.android.material.tabs.TabLayoutMediator


class UserProductInfoFragment : Fragment() {
    private var _binding: FragmentUserProductInfoBinding? = null
    private val binding get() = _binding!!
    private val args:UserProductInfoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info, container, false)

        onClickNavigationIconBackStack()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        val detailList = Storage.detailList
        binding.viewPagerUserProductInfo.adapter = ProductTabAdapter(this, detailList)
        TabLayoutMediator(binding.tabLayoutUserProductInfo, binding.viewPagerUserProductInfo) { tab, pos ->
            tab.text = detailList[pos]
        }.attach()
        toolbarSetting()
    }

    private fun toolbarSetting() {
        binding.toolBarUserProductInfo.title = args.productNameMethod?.productName
    }


    // sehoon 툴바 네비게이션 클릭 메서드
    private fun onClickNavigationIconBackStack() {
        binding.toolBarUserProductInfo.apply {
            setNavigationOnClickListener {
                val action = UserProductInfoFragmentDirections.actionUserProductInfoToNavigationHome()
                findNavController().navigate(action)
            }
        }
    }


}

