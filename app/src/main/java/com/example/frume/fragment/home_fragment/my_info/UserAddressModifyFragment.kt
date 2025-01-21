package com.example.frume.fragment.home_fragment.my_info

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserAddressModifyBinding
import com.example.frume.fragment.home_fragment.my_info.my_profile_setting.UserAddressManageFragmentDirections

class UserAddressModifyFragment : Fragment() {
    private var _binding: FragmentUserAddressModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_address_modify, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로 가기 버튼 호출
        onClickToolbar()
        // 저장 버튼 호출
        onClickConfirmBtn()
        // 텍스트 입력 시작 시 에러 메시지 제거
        setTextChangeListener()
        // 툴바 메뉴 삭제 버튼 클릭
        onClickAddressDeleteBtn()
    }

    // 네비게이션 뒤로가기 메서드
    private fun onClickToolbar() {
        binding.toolBarUserAddressModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 저장 버튼 클릭 처리
    private fun onClickConfirmBtn() {
        binding.buttonUserAddressModifyArrivalAdd.setOnClickListener {
            clearErrors()

            if (validateInputs()) {
                // 유효한 경우, 저장 및 네비게이션
                findNavController().navigateUp()
            }
        }
    }

    // 입력 필드 유효성 검사
    private fun validateInputs(): Boolean {
        var isValid = true

        // 배송지 이름 검사
        if (binding.textInputLayoutUserAddressModifyArrivalName.editText?.text.toString().isEmpty()) {
            binding.textInputLayoutUserAddressModifyArrivalName.error = "배송지 이름을 입력해 주세요"
            isValid = false
        }

        // 이름 검사
        if (binding.textInputLayoutUserAddressModifyUserName.editText?.text.toString().isEmpty()) {
            binding.textInputLayoutUserAddressModifyUserName.error = "이름을 입력해 주세요"
            isValid = false
        }

        // 휴대폰 번호 검사 11자리만
        val phoneNumber = binding.textInputLayoutUserAddressModifyPhoneNumber.editText?.text.toString()
        if (phoneNumber.isEmpty()) {
            binding.textInputLayoutUserAddressModifyPhoneNumber.error = "휴대폰 번호를 입력해 주세요"
            isValid = false
        } else if (phoneNumber.length != 11) {
            isValid = false
        }

        // 상세 주소 검사
        if (binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.text.toString().isEmpty()) {
            binding.textInputLayoutUserModifyAddressAddDetailAddress.error = "상세 주소를 입력해 주세요"
            isValid = false
        }

        return isValid
    }

    // 에러 메시지 초기화
    private fun clearErrors() {
        binding.textInputLayoutUserAddressModifyArrivalName.error = null
        binding.textInputLayoutUserAddressModifyUserName.error = null
        binding.textInputLayoutUserAddressModifyPhoneNumber.error = null
        binding.textInputLayoutUserModifyAddressAddDetailAddress.error = null
    }

    // 텍스트 입력 시작 시 에러 메시지 제거
    private fun setTextChangeListener() {
        binding.textInputLayoutUserAddressModifyArrivalName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserAddressModifyArrivalName.error = null
            }
        })

        binding.textInputLayoutUserAddressModifyUserName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserAddressModifyUserName.error = null
            }
        })

        binding.textInputLayoutUserAddressModifyPhoneNumber.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val phoneNumber = editable.toString()
                // 11자리 이상 입력되지 않도록 제한
                if (phoneNumber.length > 11) {
                    editable?.delete(11, phoneNumber.length)
                }
                binding.textInputLayoutUserAddressModifyPhoneNumber.error = null
            }
        })

        binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserModifyAddressAddDetailAddress.error = null
            }
        })
    }

    // 툴바 메뉴 삭제 클릭 처리
    private fun onClickAddressDeleteBtn() {
        binding.toolBarUserAddressModify.setOnMenuItemClickListener { menu ->
            when (menu.itemId){
                R.id.menu_user_address_manage_delete -> {
                    showDeleteConfirmationDialog()  // 아이템 클릭 시 다이얼로그 띄우기
                    return@setOnMenuItemClickListener true
                }
                else -> true
            }
        }
    }

    // 배송지 삭제 확인 다이얼로그
    private fun showDeleteConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("배송지 삭제")
            .setMessage("이 배송지를 삭제하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                // 배송지 삭제 처리
                Toast.makeText(requireContext(), "배송지가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                // 삭제 후 이전 화면으로 돌아가기
                findNavController().navigateUp()
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
    }
}

