package com.example.frume.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserSignUpBinding


class UserSignUpFragment : Fragment() {
    private var _binding: FragmentUserSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_sign_up, container, false)
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

    private fun setLayout() {
        // 툴바 세팅(뒤로 가기)
        settingToolbar()
        // 회원 가입 완료 및 홈화면으로 이동
        onClickSignUpButton()

    }

    // sehoon 회원가입 완료 버튼 클릭 메서드
    private fun onClickSignUpButton() {
        binding.buttonUserLogin.setOnClickListener {
            val action = UserSignUpFragmentDirections.actionUserSignupToUserLogin()
            findNavController().navigate(action)
        }
    }

    // sehoon 툴바 뒤로가기 클릭 메서드
    private fun settingToolbar() {
        binding.toolbarUserSignUp.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}