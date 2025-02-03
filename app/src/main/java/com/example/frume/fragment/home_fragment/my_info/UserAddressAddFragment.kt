package com.example.frume.fragment.home_fragment.my_info

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.activity.AddressActivity
import com.example.frume.databinding.FragmentUserAddressAddBinding
import com.example.frume.activity.HomeActivity
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.service.UserDeliveryAddressService
import com.example.frume.util.DeliveryDefaultAddressBoolType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserAddressAddFragment : Fragment() {
    // 유저 문서 아이디 가져오기 위해 hj
    private var _binding: FragmentUserAddressAddBinding? = null
    private val binding get() = _binding!!
    lateinit var homeActivity: HomeActivity

    // 전역 변수 선언 (이 변수에 주소를 저장)
    private val getCustomerUserAddressAdd = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_address_add, container, false)

        // activity 설정 hj
        homeActivity = activity as HomeActivity

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLayout()
    }

    @SuppressLint("NewApi")
    private fun setLayout() {
        onClickToolbar()
        onClickSaveButton()
        setAddressFieldAddClickListener()
    }

    // sehoon 툴바 클릭 메서드
    private fun onClickToolbar() {
        binding.toolBarUserAddressModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 주소 선택을 위한 결과 처리
    @RequiresApi(Build.VERSION_CODES.O)
    private val getAddressAddResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val address = result.data?.getStringExtra("data")
                // val postNumber = result.data?.getStringExtra("postData")

                // 주소를 TextView에 반영
                binding.textViewUserAddressModifyShowAddress.setText(address)

                // 전역 변수에 주소 값 저장
                getCustomerUserAddressAdd["DetailedAddress"] = binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.text.toString().trim()
                getCustomerUserAddressAdd["BasicAddress"] = address ?: ""
                getCustomerUserAddressAdd["PostNumber"] = address ?: ""

                // 디버깅 로그 출력
                Log.d("test100", "customerUserAddress: $getCustomerUserAddressAdd")
            }
        }

    // 주소 선택 버튼 클릭 리스너
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAddressFieldAddClickListener() {
        binding.textViewUserAddressModifyShowAddress.setOnClickListener {
            // AddressActivity로 이동
            val intent = Intent(requireContext(), AddressActivity::class.java)
            getAddressAddResult.launch(intent)  // 주소 찾기 화면으로 이동
        }
    }

    // 저장 버튼 클릭 메서드
    private fun onClickConfirmBtn() {
        val isCheckedState = binding.checkboxUserAddressModifyDefaultAddress.isChecked

        // val postNumber = 12345

        val deliveryAddressModel = DeliveryAddressModel().apply {
            deliveryAddressReceiverName = binding.textInputLayoutUserAddressModifyUserName.editText?.text.toString()
            deliveryAddressName = binding.textInputLayoutUserAddressModifyArrivalName.editText?.text.toString()
            deliveryAddressBasicAddress = binding.textViewUserAddressModifyShowAddress.text.toString()
            deliveryAddressDetailAddress = binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.text.toString()
            deliveryAddressPhoneNumber = binding.textInputLayoutUserAddressModifyPhoneNumber.editText?.text.toString()
            // deliveryAddressPostNumber = binding.textViewUserAddressModifyShowPostNumber.text.toString()
            deliveryAddressIsDefaultAddress = if (isCheckedState) {
                DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_DEFAULT
            } else {
                DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_NOT_DEFAULT
            }
        }

        // 새 배송지를 추가한다. 그리고 새 문서 Doc ID를 리턴받는다
        val newDocId = UserDeliveryAddressService.addDeliveryAddress(homeActivity.loginUserDocumentId, deliveryAddressModel)

        if(isCheckedState) {
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async (Dispatchers.IO){
                    // 현재 기본 배송지의 기본배송지 여부 값을 false로 바꿔줘야 함
                    // 리턴받았던 newDoc ID를 여기서 사용한다
                    UserDeliveryAddressService.changeDefaultStateToFalse(homeActivity.loginUserDocumentId, newDocId)
                }
                work1.await()
            }
        }
    }

    // 입력값(배송지 이름, 이름, 휴대폰 번호, 주소, 상세 주소) 유효성 검사
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

            // 주소 유효성 검사
            val address = binding.textViewUserAddressModifyShowAddress.text.toString()
            if (address.isBlank()){
                binding.textViewUserAddressModifyShowAddress.error = "주소를 입력해주세요."
                isValid = false
            } else {
                binding.textViewUserAddressModifyShowAddress.error = null
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
                onClickConfirmBtn()
                Toast.makeText(requireContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                // 저장 로직 추가
                findNavController().navigateUp()
            }
        }
    }

}