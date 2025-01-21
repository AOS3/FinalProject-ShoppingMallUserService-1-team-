package com.example.frume.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserSignUpBinding
import com.example.frume.util.validateInput


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
        onClickIdValidateBtn()
    }

    // sehoon 검사 후 db에 저장
    private fun insertDB() {
        // db 저장 코드

    }

    // sehoon 로그인 화면으로 이동
    private fun moveToLogin() {
        val action = UserSignUpFragmentDirections.actionUserSignupToUserLogin()
        findNavController().navigate(action)
    }

    // sehoon 회원가입 완료 버튼 클릭 메서드
    private fun onClickSignUpButton() {
        binding.buttonUserSignUp.setOnClickListener {
            val emptyInput = emptyEditText() // 값이 비어있는지 확인
            val passwordCheck = integrationPassword() // 비밀번호 통합 확인
            val isCheckBox = isCheckBox()

            if (emptyInput && passwordCheck && isCheckBox) {
                insertDB()
                // db에 저장 후 네비게이션 이동 코루틴 사용!
                moveToLogin()
            } else {
                Toast.makeText(requireContext(), "일치x", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // sehoon 툴바 뒤로가기 클릭 메서드
    private fun settingToolbar() {
        binding.toolbarUserSignUp.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // sehoon 회원가입 유효성 검사
    private fun onClickIdValidateBtn() {
        binding.buttonUserSignUpCheck.setOnClickListener {
            if (binding.textFieldUserSignUpId.editText?.text.toString().isEmpty()) {
                binding.textFieldUserSignUpId.isErrorEnabled = true
                binding.textFieldUserSignUpId.error = "아이디를 입력하세요"
            } else {
                if (validateIdInput(binding.textFieldUserSignUpId.editText?.text.toString())) {
                    binding.textFieldUserSignUpId.isErrorEnabled = false
                    binding.textFieldUserSignUpId.isEnabled = false
                    binding.buttonUserSignUpCheck.isEnabled = false
                } else {
                    binding.textFieldUserSignUpId.isErrorEnabled = true
                    binding.textFieldUserSignUpId.error = "소문자, 숫자를 포함한 5~15자리를 입력하세요"
                }
            }
        }
    }

    // sehoon 주소 필드 유효성 검사
    private fun validateAddressField(): Boolean {
        val addressText = binding.textFieldUserSignUpAddress.text.toString().trim()
        return if (addressText.isEmpty() || addressText == "클릭해서 주소 설정") {
            // 에러 처리
            binding.textFieldUserSignUpAddress.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.red50)
            )
            false
        } else {
            true
        }
    }

    // sehoon 입력 빈칸 확인
    private fun emptyEditText(): Boolean {
        binding.apply {
            val isValid = listOf(
                textFieldUserSignUpId.validateInput("아이디를 입력해주세요."),
                textFieldUserSignUpPw.validateInput("비밀번호를 입력해주세요."),
                textFieldUserSignUpPwCheck.validateInput("비밀번호 확인을 입력해주세요."),
                textFieldUserSignUpEmail.validateInput("이메일을 입력해주세요."),
                textFieldUserSignUpName.validateInput("이름을 입력해주세요."),
                textFieldUserSignUpRRNFirst.validateInput("주민번호 앞 입력해주세요."),
                textFieldUserSignUpRRNLast.validateInput("주민번호 뒤 입력해주세요."),
                textFieldUserSignUpPhoneNumber.validateInput("전화번호를 입력해주세요."),
                textFieldUserSignUpDetailAddress.validateInput("상세주소를 입력해주세요."),
                validateAddressField()
            ).all { it }
            return isValid
        }
    }

    // sehoon 아이디 유효성 검사
    private fun validateIdInput(input: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{5,15}$".toRegex()
        return regex.matches(input)
    }

    // sehoon 비밀번호 정규식
    private fun validatePwInput(input: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[a-z\\d@\$!%*?&]{8,16}$".toRegex()
        return regex.matches(input)
    }

    // sehoon 비밀번호 유효성 검사
    private fun validatePw(): Boolean {
        val validatePw = validatePwInput(binding.textFieldUserSignUpPw.editText?.text.toString())
        if (!validatePw) {
            binding.textFieldUserSignUpPw.isErrorEnabled = true
            binding.textFieldUserSignUpPw.error = " 소문자, 숫자, 특수문자 포함한 8~16자리를 입력하세요"
        } else {
            binding.textFieldUserSignUpPw.isErrorEnabled = false
        }
        return validatePw
    }

    // sehoon 비밀번호 일치 확인
    private fun validatePwCheck(): Boolean {
        val firstPw = binding.textFieldUserSignUpPw.editText?.text.toString().trim()
        val secondPw = binding.textFieldUserSignUpPwCheck.editText?.text.toString().trim()

        return if (firstPw == secondPw) {
            // 비밀번호가 일치할 경우 에러 제거
            binding.textFieldUserSignUpPwCheck.isErrorEnabled = false
            true
        } else {
            // 비밀번호가 일치하지 않을 경우 에러 메시지 설정
            binding.textFieldUserSignUpPwCheck.isErrorEnabled = true
            binding.textFieldUserSignUpPwCheck.error = "비밀번호가 일치하지 않습니다."
            false
        }
    }

    // sehoon 체크박스 유무 검사
    private fun isCheckBox(): Boolean {
        val checkBox = binding.checkBoxUserSignUpTerms
        if (!checkBox.isChecked) {
            checkBox.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.red50)
            )
            return false
        } else {
            checkBox.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            return true
        }
    }

    // sehoon 비밀번호 통합 검사
    private fun integrationPassword(): Boolean {
        val isPwValid = validatePw() // 비밀번호 유효성 검사
        val isPwMatched = validatePwCheck() // 비밀번호 일치 검사

        return isPwValid && isPwMatched
    }
}