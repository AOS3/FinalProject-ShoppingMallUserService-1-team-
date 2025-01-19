package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoManageBinding
import com.example.frume.databinding.FragmentUserPwModifyBinding


class UserPwModifyFragment : Fragment() {
    private var _binding: FragmentUserPwModifyBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_pw_modify,
            container,
            false
        )
        onClickButtonSubmit()


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickButtonSubmit()
        onClickToolbar()
    }

    // sehoon 저장버튼 클릭 메서드
    private fun onClickButtonSubmit() {
        binding.buttonUserPwModifySubmit.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // sehoon 툴바 네비게이션 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserPwModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}