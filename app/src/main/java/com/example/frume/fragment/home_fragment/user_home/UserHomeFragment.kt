package com.example.frume.fragment.home_fragment.user_home

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
import com.example.frume.databinding.FragmentUserHomeBinding
import com.example.frume.fragment.user_fragment.product_info.UserProductInfoFragmentArgs
import com.google.android.material.tabs.TabLayoutMediator

/*import com.example.frume.fragment.FullSubFragmentName*/

class UserHomeFragment : Fragment() {
    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home, container, false)
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

    // 화면 구성
    private fun setLayout() {
        setTabLayout()
        moveToSearchScreen()
    }

    // 검색화면으로 이동
    private fun moveToSearchScreen() {
        binding.toolbarUserHome.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuSearch -> {
                    // 검색 화면으로 이동
                    val action = UserHomeFragmentDirections.actionNavigationHomeToUserSearch()
                    findNavController().navigate(action)

                    // 이동한 후 UserSearchFragment에서 포커스와 키보드를 처리하도록
                    true
                }
                else -> true
            }
        }
    }


    // 탭 레이아웃 이름 설정
    private fun setTabLayout() {
        val category = Storage.categoryList
        binding.viewpagerUserHomeMain.adapter = HomeTabAdapter(this, category)
        TabLayoutMediator(binding.tabUserHome, binding.viewpagerUserHomeMain) { tab, pos ->
            tab.text = category[pos]
        }.attach()
    }
}
