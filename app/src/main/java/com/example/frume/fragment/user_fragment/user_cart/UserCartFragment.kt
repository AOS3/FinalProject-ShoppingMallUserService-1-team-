package com.example.frume.fragment.user_fragment.user_cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCartBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserCartFragment() : Fragment() {

    lateinit var fragmentUserCartBinding: FragmentUserCartBinding
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentUserCartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_cart, container, false)

        // ViewPager2와 TabLayout 설정
        viewPager = fragmentUserCartBinding.viewpagerUserCartMain
        tabLayout = fragmentUserCartBinding.tabbarUserCart

        val pagerAdapter = UserCartPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // TabLayout과 ViewPager2를 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "1회 구매"
                1 -> tab.text = "정기 구독"
            }
        }.attach()

        return fragmentUserCartBinding.root
    }

    inner class UserCartPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2  // 두 개의 탭 (1회 구매, 정기 구독)
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserCartFragment1() // 첫 번째 탭: 1회 구매
                1 -> UserCartFragment2() // 두 번째 탭: 정기 구독
                else -> throw IllegalStateException("Invalid position")
            }
        }
    }
}
