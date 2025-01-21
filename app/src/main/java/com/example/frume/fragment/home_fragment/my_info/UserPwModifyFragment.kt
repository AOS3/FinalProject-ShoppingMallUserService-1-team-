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
import com.google.android.material.textfield.TextInputLayout


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

    // sehoon 완료버튼 클릭 메서드
    private fun onClickButtonSubmit() {
        binding.buttonUserPwModifySubmit.setOnClickListener {
            val isValidPw = validatePasswords()
            if(!isValidPw) return@setOnClickListener
            findNavController().navigateUp()
        }
    }

    // sehoon 툴바 네비게이션 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserPwModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 비밀번호 유효성 검사 함수
    private fun validatePasswords(): Boolean {
        var isValid = true
        // DB에서 가져온 회원 비밀번호
        var userPw = "!q123456"

        // 현재 비밀번호
        val currentPw = binding.textInputLayoutUserPwModify.editText?.text.toString()

        // 새 비밀번호
        val newPw = binding.textInputLayoutUserNewPwModify.editText?.text.toString()

        // 새 비밀번호 확인
        val confirmPw = binding.textInputLayoutUserNewPwVerify.editText?.text.toString()

        // 정규식: 8~15자, 소문자와 특수문자 포함
        val pwPattern = Regex("^(?=.*[a-z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,15}$")

        // 현재 비밀번호 유효성 검사
        when {
            !currentPw.matches(pwPattern) -> {
                showError(binding.textInputLayoutUserPwModify, "비밀번호는 8~15자, 소문자와 특수문자를 포함해야 합니다.")
                isValid = false
            }
            currentPw != userPw -> {
                showError(binding.textInputLayoutUserPwModify, "현재 비밀번호가 일치하지 않습니다.")
                isValid = false
            }
            else -> clearError(binding.textInputLayoutUserPwModify)
        }

        // 새 비밀번호 유효성 검사
        when {
            !newPw.matches(pwPattern) -> {
                showError(binding.textInputLayoutUserNewPwModify, "새 비밀번호는 8~15자, 소문자와 특수문자를 포함해야 합니다.")
                isValid = false
            }
            currentPw == newPw -> {
                showError(binding.textInputLayoutUserNewPwModify, "새 비밀번호는 현재 비밀번호와 달라야 합니다.")
                isValid = false
            }
            else -> clearError(binding.textInputLayoutUserNewPwModify)
        }

        // 새 비밀번호 확인 검사
        if (newPw != confirmPw) {
            showError(binding.textInputLayoutUserNewPwVerify, "새 비밀번호가 일치하지 않습니다.")
            isValid = false
        } else {
            clearError(binding.textInputLayoutUserNewPwVerify)
        }

        return isValid
    }



    // 에러 메시지 표시
    private fun showError(inputLayout: TextInputLayout, message: String) {
        inputLayout.error = message
    }

    // 에러 메시지 초기화
    private fun clearError(inputLayout: TextInputLayout) {
        inputLayout.error = null
    }


}