package com.example.frume.fragment.home_fragment.user_home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.databinding.FragmentUserSearchBinding
import com.example.frume.util.ProductCategoryDetailType


class UserSearchFragment : Fragment() {
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //   setHasOptionsMenu(true)
        onClickSearchView()
        onClickToolbarNavigation()
    }


    // sehoon 검색 결과 화면 이동
    private fun showSearchResultFragment(query: String) {
        val action = UserSearchFragmentDirections.actionUserSearchToUserCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_SEARCH, query)
        findNavController().navigate(action)
    }

    // sehoon 홈 화면으로 이동
    private fun onClickToolbarNavigation() {
        binding.toolbarUserSearch.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun onClickSearchView() {
        binding.searchViewUserSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    // Search 버튼을 눌렀을 때 실행할 동작
                    showSearchResultFragment(query) // query 값을 사용하여 함수 호출
                } else {
                    Log.d("123", query.toString())
                    Toast.makeText(requireContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
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