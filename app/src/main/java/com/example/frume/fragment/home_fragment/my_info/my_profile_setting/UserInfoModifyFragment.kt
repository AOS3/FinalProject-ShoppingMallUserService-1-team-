package com.example.frume.fragment.home_fragment.my_info.my_profile_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoModifyBinding
import com.example.frume.fragment.user_fragment.product_info.UserProductInfoFragmentDirections
import com.example.frume.home.HomeActivity
import com.example.frume.model.UserModel
import com.example.frume.repository.UserRepository
import com.example.frume.service.UserService
import com.example.frume.vo.UserVO
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// sehoon 유저 정보 수정 화면
class UserInfoModifyFragment : Fragment() {
    private var _binding: FragmentUserInfoModifyBinding? = null
    private val binding get() = _binding!!

    lateinit var homeActivity: HomeActivity
    // 사용자 정보를 담을 변수
    lateinit var userModel: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeActivity = activity as HomeActivity

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
        settingInputData()
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

            saveUserInfo() // 사용자 정보 저장
            findNavController().navigateUp()
        }
    }

    // 필드 유효성 검사 메서드
    private fun validateFields(): Boolean {
        var isValid = true

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

    // 사용자 정보를 Firestore에 저장하는 메서드
    // 주소 웹뷰 구현 후 추가 필요.
    private fun saveUserInfo() {

        // 입력받은 데이터
        val userName = binding.textInputLayoutUserInfoModifyUserName.editText?.text.toString()
        val phoneNumber = binding.textInputLayoutUserInfoModifyPhoneNumber.editText?.text.toString()
        val detailAddress = binding.textInputLayoutUserInfoModifyDetailAddress.editText?.text.toString()

        // 수정할 데이터를 VO에 담는다
        val userModel = UserModel().apply {
            customerUserDocId = homeActivity.loginUserDocumentId
            customerUserName = userName
            customerUserPhoneNumber = phoneNumber
            customerUserAddress["DetailedAddress"] = detailAddress
        }

        // Firestore 저장
        CoroutineScope(Dispatchers.IO).launch {
            UserService.updateUserData(userModel) // UserService 사용

        }
    }

    // 데이터를 읽어와 입력 요소를 채워준다.
    fun settingInputData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                //UserService.selectUserDataByUserDocumentIdOne(boardActivity.loginUserDocumentId)
                UserService.selectUserDataByuserDocumentId(homeActivity.loginUserDocumentId)
            }
            userModel = work1.await()

            binding.apply {

                textInputLayoutUserInfoModifyUserName.editText?.setText(userModel?.customerUserName ?: "")
                textInputLayoutUserInfoModifyPhoneNumber.editText?.setText(userModel?.customerUserPhoneNumber ?: "")
                textViewUserInfoModifyShowAddress.text = userModel.customerUserAddress["BasicAddress"]
                textViewUserInfoModifyShowPostNumber.text = userModel.customerUserAddress["PostNumber"]
                textInputLayoutUserInfoModifyDetailAddress.editText?.setText(userModel?.customerUserAddress?.get("DetailedAddress") ?: "")

            }

        }
    }

}

