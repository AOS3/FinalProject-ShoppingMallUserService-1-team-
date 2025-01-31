package com.example.frume.fragment.home_fragment.user_home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.view.inputmethod.InputMethodManager

import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.databinding.FragmentUserSearchBinding
import com.example.frume.activity.HomeActivity
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

        // SearchView에 포커스를 주고 키보드를 올리기
        binding.searchViewUserSearch.requestFocus()  // SearchView에 포커스 주기
        // (activity as? HomeActivity)?.showSoftInput(view)  // 키보드 올리기

        // SearchView의 EditText에 포커스 변경 리스너 설정
        val editText = binding.searchViewUserSearch.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // EditText가 포커스를 받은 경우
                (activity as? HomeActivity)?.showSoftInput(view)  // 키보드 올리기
            } else {
                // EditText가 포커스를 잃은 경우
                (activity as? HomeActivity)?.hideSoftInput()  // 키보드 내리기
            }
        }


        showKeyboard()
        showKeyboard2()

        onClickSearchView()
        onClickToolbarNavigation()
    }


    // 검색 결과 화면 이동

    private fun showKeyboard2() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.searchViewUserSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showKeyboard() {
        binding.searchViewUserSearch.requestFocus()
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    // sehoon 검색 결과 화면 이동

    private fun showSearchResultFragment(query: String) {
        val action = UserSearchFragmentDirections.actionUserSearchToUserCategoryDetail(ProductCategoryDetailType.PRODUCT_CATEGORY_SEARCH, query)
        findNavController().navigate(action)
    }

    // 홈 화면으로 이동
    private fun onClickToolbarNavigation() {
        binding.toolbarUserSearch.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // SearchView의 텍스트 검색 시 이벤트
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
