package com.example.frume.adapter


import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.frume.fragment.home.home_tab_first_screen.UserHomeTabFirstFragment
import com.example.frume.fragment.home.home_tab_second_screen.UserHomeTabSecondFragment

// 홈 화면 최상단 탭 레이아웃 어댑터

class HomeTabAdapter(fragment: Fragment, private val categories: List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = categories.size

    // 0이면 FirstFragment , 나머지 SecFragment보여주기
    override fun createFragment(position: Int): Fragment {
        Log.d("HomeTabAdapter", "createFragment position: $position, category: ${categories.getOrNull(position)}")

        return when (position) {
            0 -> UserHomeTabFirstFragment.newInstance()
            else -> UserHomeTabSecondFragment.newInstance(position)
        }
    }

}