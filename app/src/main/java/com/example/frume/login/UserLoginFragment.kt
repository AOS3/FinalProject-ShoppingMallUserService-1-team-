package com.example.frume.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserLoginBinding
import com.example.frume.home.HomeActivity


class UserLoginFragment : Fragment() {
    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_login, container, false)
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
        onClickLoginBtn()
        onClickSignUpBtn()
        onClickNonMemberLoginBtn()
    }

    // sehoon 홈 화면 이동 메서드
    private fun moveToHomeScreen() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }

    // sehoon 로그인 버튼 클릭 메서드
    private fun onClickLoginBtn() {
        binding.buttonUserLogin.setOnClickListener {
            moveToHomeScreen()
        }
    }

    // sehoon 비회원 접속 버튼 클릭 메서드
    private fun onClickNonMemberLoginBtn() {
        binding.buttonUserLoginNonMember.setOnClickListener {
            moveToHomeScreen()
        }
    }

    // sehoon 회원가입 버튼 클릭 메서드
    private fun onClickSignUpBtn() {
        binding.textViewUserLoginSignUpButton.setOnClickListener {
            val action = UserLoginFragmentDirections.actionUserLoginToUserSignup()
            findNavController().navigate(action)
        }
    }

}