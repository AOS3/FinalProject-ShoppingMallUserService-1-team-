package com.example.frume.fragment.my_info

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.activity.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserPwModifyBinding
import com.example.frume.service.UserService
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserPwModifyFragment : Fragment() {
    private var _binding: FragmentUserPwModifyBinding? = null
    private val binding get() = _binding!!
    lateinit var homeActivity: HomeActivity

    // 새 비밀번호를 저장할 멤버 변수
    private var newPw: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeActivity = activity as HomeActivity

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

//    // sehoon 완료버튼 클릭 메서드
//    private fun onClickButtonSubmit() {
//        binding.buttonUserPwModifySubmit.setOnClickListener {
//            val isValidPw = validatePasswords()
//            if(!isValidPw) return@setOnClickListener
//
//            // 비밀번호 유효성 검사 완료된 경우
//            updateUserPassword()
//            findNavController().navigateUp()
//        }
//    }

    private fun onClickButtonSubmit() {
        binding.buttonUserPwModifySubmit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val isValidPw = validatePasswords() // suspend 함수 호출
                if (!isValidPw) return@launch

                // 비밀번호 유효성 검사 완료된 경우
                updateUserPassword()
                findNavController().navigateUp()
            }
        }
    }

    // sehoon 툴바 네비게이션 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserPwModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 비밀번호 유효성 검사 함수
    private suspend fun validatePasswords(): Boolean {
        var isValid = true

        // DB에서 가져온 회원 비밀번호
        // var userPw = "!q123456"
        var userPw = UserService.selectUserPasswordByUserDocId(homeActivity.loginUserDocumentId)




        // 현재 비밀번호
        val currentPw = binding.textInputLayoutUserPwModify.editText?.text.toString()

        // 새 비밀번호 저장
        newPw = binding.textInputLayoutUserNewPwModify.editText?.text.toString()

        // 새 비밀번호 확인
        val confirmPw = binding.textInputLayoutUserNewPwVerify.editText?.text.toString()

        // 정규식: 8~15자, 소문자와 특수문자 포함
        val pwPattern = Regex("^(?=.*[a-z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,15}$")

        Log.d("test100", "UserPwModifyFragment -> 현재 비밀번호 : ${userPw}")
        Log.d("test100", "UserPwModifyFragment -> 새 비밀번호 : ${newPw}")

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

    // 비밀번호 수정 처리 메서드
    private fun updateUserPassword() {
        // 코루틴을 사용해 suspend 함수 호출
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // DB에 비밀번호 갱신
                UserService.updateUserPassword(homeActivity.loginUserDocumentId, newPw)

                // 토큰 삭제
                val sharedPreferences = requireContext().getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("token") // 저장된 토큰 삭제
                editor.apply()

                // 확인용
                // 성공 처리
                //Toast.makeText(requireContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // 에러 처리
                //Toast.makeText(requireContext(), "비밀번호 변경 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


}