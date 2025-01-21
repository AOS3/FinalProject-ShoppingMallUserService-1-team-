package com.example.frume.fragment.home_fragment.my_info.my_profile_setting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoModifyBinding
import com.google.android.material.textfield.TextInputLayout

class UserInfoModifyFragment : Fragment() {
    private var _binding: FragmentUserInfoModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_modify, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 저장 버튼 호출
        onClickButtonSubmit()
        // 뒤로 가기 버튼 호출
        onClickNavigationIcon()
        // 텍스트 입력 시작 시 에러 메시지 제거
        setTextChangeListener()
    }

    // 네비게이션 아이콘 클릭 리스너
    fun onClickNavigationIcon() {
        binding.toolbarUserInfoModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 저장 버튼 클릭 리스너
    private fun onClickButtonSubmit() {
        binding.buttonUserInfoManageModifyUserInfo.setOnClickListener {
            // 이전 에러 메시지 지우기
            clearErrors()

            // 입력 값 유효성 검사
            if (validateInputs()) {
                // 유효한 경우, 네비게이션
                findNavController().navigateUp()
            }
        }
    }

    // 입력 필드 유효성 검사
    private fun validateInputs(): Boolean {
        var isValid = true

        // 배송지 이름 검사
        if (binding.textInputLayoutUserInfoModifyArrivalName.editText?.text.toString().isEmpty()) {
            binding.textInputLayoutUserInfoModifyArrivalName.error = "배송지 이름을 입력해 주세요"
            isValid = false
        }

        // 이름 검사
        if (binding.textInputLayoutUserInfoModifyUserName.editText?.text.toString().isEmpty()) {
            binding.textInputLayoutUserInfoModifyUserName.error = "이름을 입력해 주세요"
            isValid = false
        }

        // 휴대폰 번호 검사 11자리만
        val phoneNumber = binding.textInputLayoutUserInfoModifyPhoneNumber.editText?.text.toString()
        if (phoneNumber.isEmpty()) {
            binding.textInputLayoutUserInfoModifyPhoneNumber.error = "휴대폰 번호를 입력해 주세요"
            isValid = false
        } else if (phoneNumber.length != 11) {
            isValid = false
        }

        // 상세 주소 검사
        if (binding.textInputLayoutUserInfoModifyDetailAddress.editText?.text.toString().isEmpty()) {
            binding.textInputLayoutUserInfoModifyDetailAddress.error = "상세 주소를 입력해 주세요"
            isValid = false
        }

        return isValid
    }

    // 에러 메시지 초기화
    private fun clearErrors() {
        binding.textInputLayoutUserInfoModifyArrivalName.error = null
        binding.textInputLayoutUserInfoModifyUserName.error = null
        binding.textInputLayoutUserInfoModifyPhoneNumber.error = null
        binding.textInputLayoutUserInfoModifyDetailAddress.error = null
    }

    // 텍스트 입력 시작 시 에러 메시지 제거
    private fun setTextChangeListener() {
        binding.textInputLayoutUserInfoModifyArrivalName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserInfoModifyArrivalName.error = null
            }
        })

        binding.textInputLayoutUserInfoModifyUserName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserInfoModifyUserName.error = null
            }
        })

        binding.textInputLayoutUserInfoModifyPhoneNumber.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val phoneNumber = editable.toString()
                // 11자리 이상 입력되지 않도록 제한
                if (phoneNumber.length > 11) {
                    editable?.delete(11, phoneNumber.length)
                }
                binding.textInputLayoutUserInfoModifyPhoneNumber.error = null
            }
        })

        binding.textInputLayoutUserInfoModifyDetailAddress.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserInfoModifyDetailAddress.error = null
            }
        })
    }
}
