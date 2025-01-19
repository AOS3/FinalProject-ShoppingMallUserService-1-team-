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
import com.example.frume.fragment.home_fragment.my_info.my_profile_setting.UserInfoModifyFragmentDirections

class UserInfoManageFragment : Fragment() {
    private var _binding: FragmentUserInfoManageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_manage, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickButtonUserInfoModify()
        onClickButtonModifyPw()
        onClickToolbar()
    }

    // sehoon 정보 수정하기 클릭 메서드
    private fun onClickButtonUserInfoModify() {
        binding.buttonUserInfoManageModifyUserInfo.setOnClickListener {
            val action = UserInfoManageFragmentDirections.actionUserInfoManageToUserInfoModifyFragment()
            findNavController().navigate(action)
        }
    }

    // sehoon 비밀번호 변경 클릭 메서드
    private fun onClickButtonModifyPw() {
        binding.textViewUserInfoManageModifyPW.setOnClickListener {
            val action = UserInfoManageFragmentDirections.actionUserInfoManageToUserPwModify()
            findNavController().navigate(action)
        }
    }

    // sehoon 툴바 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserInfoModify.setOnClickListener{
            findNavController().navigateUp()
        }
    }

}