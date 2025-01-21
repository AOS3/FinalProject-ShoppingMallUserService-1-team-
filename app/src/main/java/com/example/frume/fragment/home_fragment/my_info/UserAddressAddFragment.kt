package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserAddressAddBinding


class UserAddressAddFragment : Fragment() {
    private var _binding: FragmentUserAddressAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_address_add, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLayout()
    }

    private fun setLayout() {
        onClickToolbar()
        onClickSaveButton()
    }

    // sehoon 툴바 클릭 메서드
    private fun onClickToolbar() {
        binding.toolBarUserAddressModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // sehoon 저장버튼 클릭 메서드
    private fun onClickConfirmBtn() {
        binding.buttonUserAddressModifyArrivalAdd.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 입력값(배송지 이름, 이름, 휴대폰 번호, 상세주로) 유효성 검사
    private fun onClickSaveButton() {
        binding.buttonUserAddressModifyArrivalAdd.setOnClickListener {
            var isValid = true

            // 배송지 이름 유효성 검사
            val arrivalName = binding.textInputLayoutUserAddressModifyArrivalName.editText?.text.toString()
            if (arrivalName.isBlank()) {
                binding.textInputLayoutUserAddressModifyArrivalName.error = "배송지 이름을 입력해주세요."
                isValid = false
            } else {
                binding.textInputLayoutUserAddressModifyArrivalName.error = null // 에러 해제
            }

            // 이름 유효성 검사
            val userName = binding.textInputLayoutUserAddressModifyUserName.editText?.text.toString()
            if (userName.isBlank()) {
                binding.textInputLayoutUserAddressModifyUserName.error = "이름을 입력해주세요."
                isValid = false
            } else {
                binding.textInputLayoutUserAddressModifyUserName.error = null
            }

            // 휴대폰 번호 유효성 검사
            val phoneNumber = binding.textInputLayoutUserAddressModifyPhoneNumber.editText?.text.toString()
            if (phoneNumber.isBlank()) {
                binding.textInputLayoutUserAddressModifyPhoneNumber.error = "휴대폰 번호를 입력해주세요."
                isValid = false
            } else {
                binding.textInputLayoutUserAddressModifyPhoneNumber.error = null
            }

            // 상세주소 유효성 검사
            val detailAddress = binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.text.toString()
            if(detailAddress.isBlank()) {
                binding.textInputLayoutUserModifyAddressAddDetailAddress.error = "상세 주소를 입력해주세요."
                isValid = false
            } else {
                binding.textInputLayoutUserAddressModifyPhoneNumber.error = null
            }

            // 모든 입력값이 유효한 경우 저장 처리
            if (isValid) {
                Toast.makeText(requireContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                // 저장 로직 추가
                findNavController().navigateUp()
            }
        }
    }

}