package com.example.frume.fragment.home_fragment.my_info.my_profile_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoModifyBinding
import com.example.frume.fragment.user_fragment.product_info.UserProductInfoFragmentDirections
import com.google.android.material.textfield.TextInputLayout


// sehoon 유저 정보 수정 화면
class UserInfoModifyFragment : Fragment() {
    private var _binding: FragmentUserInfoModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_modify, container, false)
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

    // sehoon 여기에 모든 메서드 넣어주세요
    private fun setLayout() {
        onClickButtonSubmit()
        onClickNavigationIcon()
    }

    // 네비게이션 아이콘 클릭 리스너
    fun onClickNavigationIcon() {
        binding.toolbarUserInfoModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // sehoon 저장버튼 클릭 메서드
    // 나의 배송지 화면으로 돌아감
    private fun onClickButtonSubmit() {
        binding.buttonUserInfoManageModifyUserInfo.setOnClickListener {
            //val action = UserInfoModifyFragmentDirections.actionUserInfoModifyToUserInfoManage()
            val isValidInfo = validateFields()
            if (!isValidInfo) return@setOnClickListener
            findNavController().navigateUp()
        }
    }

    // 필드 유효성 검사 메서드
    private fun validateFields(): Boolean {
        var isValid = true
        // 배송지 이름 검사
        if (binding.textInputLayoutUserInfoModifyArrivalName.editText?.text.isNullOrBlank()) {
            showError(binding.textInputLayoutUserInfoModifyArrivalName, "배송지 이름을 입력하세요.")
            isValid = false
        } else {
            clearError(binding.textInputLayoutUserInfoModifyArrivalName)
        }

        // 이름 검사 (2글자 이상)
        val userName = binding.textInputLayoutUserInfoModifyUserName.editText?.text.toString()
        if (userName.length < 2) {
            showError(binding.textInputLayoutUserInfoModifyUserName, "이름은 2글자 이상이어야 합니다.")
            isValid = false
        } else {
            clearError(binding.textInputLayoutUserInfoModifyUserName)
        }

        // 휴대폰 번호 검사 (11자 이상)
        val phoneNumber = binding.textInputLayoutUserInfoModifyPhoneNumber.editText?.text.toString()
        if (phoneNumber.length < 11) {
            showError(binding.textInputLayoutUserInfoModifyPhoneNumber, "휴대폰 번호는 11자 이상이어야 합니다.")
            isValid = false
        } else {
            clearError(binding.textInputLayoutUserInfoModifyPhoneNumber)
        }

        // 상세 주소 검사
        if (binding.textInputLayoutUserInfoModifyDetailAddress.editText?.text.isNullOrBlank()) {
            showError(binding.textInputLayoutUserInfoModifyDetailAddress, "상세 주소를 입력하세요.")
            isValid = false
        } else {
            clearError(binding.textInputLayoutUserInfoModifyDetailAddress)
        }

        return isValid
    }

    // 에러 표시 메서드
    private fun showError(inputLayout: TextInputLayout, errorMessage: String) {
        inputLayout.error = errorMessage
    }

    // 에러 제거 메서드
    private fun clearError(inputLayout: TextInputLayout) {
        inputLayout.error = null
    }

}

