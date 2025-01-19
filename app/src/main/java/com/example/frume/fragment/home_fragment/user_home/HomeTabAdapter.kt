package com.example.frume.fragment.home_fragment.user_home


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// 홈 화면 최상단 탭 레이아웃 어댑터

class HomeTabAdapter(
    fragment: Fragment,
    private val categories: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = categories.size

    // 0이면 FirstFragment , 나머지 SecFragment보여주기
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserHomeTabFirstFragment.newInstance()
            else -> UserHomeTabSecondFragment.newInstance(position)
        }
    }
}