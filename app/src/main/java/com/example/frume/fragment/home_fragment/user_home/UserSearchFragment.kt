package com.example.frume.fragment.home_fragment.user_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserSearchBinding


class UserSearchFragment : Fragment() {
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    lateinit var homeActivity: HomeActivity
    private lateinit var searchView: SearchView
    private lateinit var searchViewItem: MenuItem
    private lateinit var mySearchViewEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        homeActivity = activity as HomeActivity
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     //   setHasOptionsMenu(true)
        test2()
        test23()
    }


    // sehoon 검색 결과 화면 이동
    private fun showSearchResultFragment(query: String) {
        val action = UserSearchFragmentDirections.actionUserSearchToUserProductShowList()
        findNavController().navigate(action)
    }

    private fun test(text: String) {
        showSearchResultFragment(text)
    }

    private fun test23() {
        binding.test.setOnClickListener {
            val action = UserSearchFragmentDirections.actionUserSearchToNavigationHome()
            findNavController().navigate(action)
        }
    }

    private fun test2() {
        binding.searchViewUserSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    // Search 버튼을 눌렀을 때 실행할 동작
                    test(query) // query 값을 사용하여 함수 호출
                }
                return false // true를 반환하면 기본 동작을 막을 수 있음
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                // text가 변경될 때 마다 실행
                return false
            }

        })
    }

}