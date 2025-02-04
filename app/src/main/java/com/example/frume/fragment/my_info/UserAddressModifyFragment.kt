package com.example.frume.fragment.my_info

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.activity.AddressActivity
import com.example.frume.activity.HomeActivity
import com.example.frume.databinding.FragmentUserAddressModifyBinding
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.service.UserDeliveryAddressService
import com.example.frume.util.DeliveryDefaultAddressBoolType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class UserAddressModifyFragment : Fragment() {
    private val args: UserAddressModifyFragmentArgs by navArgs()
    private var _binding: FragmentUserAddressModifyBinding? = null
    private val binding get() = _binding!!

    lateinit var homeActivity: HomeActivity

    var deliveryAddressModel: DeliveryAddressModel? = null


    // 전역 변수 선언 (이 변수에 주소를 저장)
    private val getCustomerUserAddressModify = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivity = activity as HomeActivity

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_address_modify,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달된 DeliveryAddressModel로 UI 업데이트
        // setAddressDataToUI()

        // 뒤로 가기 버튼 호출
        onClickToolbar()
        // 저장 버튼 호출
        onClickConfirmBtn()
        // 텍스트 입력 시작 시 에러 메시지 제거
        setTextChangeListener()
        // 툴바 메뉴 삭제 버튼 클릭
        onClickAddressDeleteBtn()

        setAddressFieldModifyClickListener()

        settingInputData()
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
            Log.d("test100", "UserAddressModifyFragment ->onClickConfirmBtn()")
            clearErrors()
            if (validateInputs()) {
                Log.d("test100", "UserAddressModifyFragment ->validateInputs() == true")
                saveUserInfo()
                // 유효한 경우, 저장 및 네비게이션
                findNavController().navigateUp()
            }
            Log.d("test100", "UserAddressModifyFragment ->onClickConfirmBtn()-> End")
        }
    }

    // 주소 선택을 위한 결과 처리
    @RequiresApi(Build.VERSION_CODES.O)
    private val getAddressModifyResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val address = result.data?.getStringExtra("data")
                binding.textViewUserAddressModifyShowAddress.setText(address)

                // 전역 변수에 주소 값 저장
                getCustomerUserAddressModify["DetailedAddress"] =
                    binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.text.toString()
                        .trim()
                getCustomerUserAddressModify["BasicAddress"] = address ?: ""
                getCustomerUserAddressModify["PostNumber"] = address ?: ""

                Log.d("test100", "customerUserAddress: $getCustomerUserAddressModify")
            }
        }

    // 주소 선택 버튼 클릭 리스너
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAddressFieldModifyClickListener() {
        binding.textViewUserAddressModifyShowAddress.setOnClickListener {
            // AddressActivity로 이동
            val intent = Intent(requireContext(), AddressActivity::class.java)
            getAddressModifyResult.launch(intent)  // 주소 찾기 화면으로 이동
        }
    }

    // 입력 필드 유효성 검사
    private fun validateInputs(): Boolean {
        var isValid = true

        if (binding.textInputLayoutUserAddressModifyArrivalName.editText?.text.toString()
                .isEmpty()
        ) {
            Log.d("test100", "1111111111validateInputs -> FALSE")
            binding.textInputLayoutUserAddressModifyArrivalName.error = "배송지 이름을 입력해 주세요"
            isValid = false
        }

        if (binding.textInputLayoutUserAddressModifyUserName.editText?.text.toString().isEmpty()) {
            Log.d("test100", "222222222222222validateInputs -> FALSE")

            binding.textInputLayoutUserAddressModifyUserName.error = "이름을 입력해 주세요"
            isValid = false
        }

        val phoneNumber =
            binding.textInputLayoutUserAddressModifyPhoneNumber.editText?.text.toString()
        if (phoneNumber.isEmpty()) {
            Log.d("test100", "3333333333333333333333validateInputs -> FALSE")

            binding.textInputLayoutUserAddressModifyPhoneNumber.error = "휴대폰 번호를 입력해 주세요"
            isValid = false
        } else if (phoneNumber.length != 11 && phoneNumber.isNotEmpty()) {
            Log.d("test100", "66666666666666666666666666 -> FALSE")
            binding.textInputLayoutUserAddressModifyPhoneNumber.error = "번호가 11자리가 아닙니다."
            isValid = false
        }

        if (binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.text.toString()
                .isEmpty()
        ) {
            Log.d("test100", "4444444444444444validateInputs -> FALSE")
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
        binding.textInputLayoutUserAddressModifyArrivalName.editText?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserAddressModifyArrivalName.error = null
            }
        })

        binding.textInputLayoutUserAddressModifyUserName.editText?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                binding.textInputLayoutUserAddressModifyUserName.error = null
            }
        })

        binding.textInputLayoutUserAddressModifyPhoneNumber.editText?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val phoneNumber = editable.toString()
                if (phoneNumber.length > 11) {
                    editable?.delete(11, phoneNumber.length)
                }
                binding.textInputLayoutUserAddressModifyPhoneNumber.error = null
            }
        })

        binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    binding.textInputLayoutUserModifyAddressAddDetailAddress.error = null
                }
            })
    }

    // 툴바 메뉴 삭제 클릭 처리
    private fun onClickAddressDeleteBtn() {
        binding.toolBarUserAddressModify.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
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
                // 배송지 상태를 비정상으로 변경
                CoroutineScope(Dispatchers.Main).launch {
                    // 배송지 상태를 2로 변경하는 함수 호출
                    deliveryAddressModel?.let {
                        UserDeliveryAddressService.markDeliveryAddressAsInvalid(
                            it.deliveryAddressDocId
                        )
                    }

                    // 처리 후, 화면에서 나가거나 적절히 목록 갱신
                    // Toast.makeText(requireContext(), "배송지가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()  // 삭제 후 이전 화면으로 돌아가기
                }
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
    }

    private fun saveUserInfo() {
        runBlocking {
            // 입력받은 데이터
            val addressArrivalName =
                binding.textInputLayoutUserAddressModifyArrivalName.editText?.text.toString()
            val addressUserName =
                binding.textInputLayoutUserAddressModifyUserName.editText?.text.toString()
            val phoneNumber =
                binding.textInputLayoutUserAddressModifyPhoneNumber.editText?.text.toString()
            val basicAdderTest = binding.textViewUserAddressModifyShowAddress.text.toString()
            val detailAddress =
                binding.textInputLayoutUserModifyAddressAddDetailAddress.editText?.text.toString()

            // 수정할 데이터를 VO에 담는다
            val deliveryModel = DeliveryAddressModel().apply {
                deliveryAddressDocId = deliveryAddressModel!!.deliveryAddressDocId
                deliveryAddressUserDocId = homeActivity.loginUserDocumentId
                deliveryAddressName = addressArrivalName
                deliveryAddressReceiverName = addressUserName
                deliveryAddressPhoneNumber = phoneNumber
                deliveryAddressBasicAddress = basicAdderTest
                deliveryAddressDetailAddress = detailAddress
            }

            // 비동기 작업 처리
            if (binding.checkboxUserAddressModifyDefaultAddress.isChecked) {
                //  추가할 배송지 기본 배송지 상태를 true로 변경한다
                deliveryModel.deliveryAddressIsDefaultAddress =
                    DeliveryDefaultAddressBoolType.DELIVERY_ADDRESS_TYPE_IS_DEFAULT
                // 지금 기본 배송지를 false로 변경
                withContext(Dispatchers.IO) {
                    // work1
                    UserDeliveryAddressService.changeDefaultStateToFalse(
                        homeActivity.loginUserDocumentId,
                        deliveryModel.deliveryAddressDocId
                    )
                    // work2
                    UserDeliveryAddressService.updateUserDeliveryAddress(
                        deliveryModel,
                        deliveryAddressModel!!.deliveryAddressDocId
                    )
                }
            } else {
                // 기본 배송지 변경이 아니면 그냥 업데이트만 처리
                withContext(Dispatchers.IO) {
                    UserDeliveryAddressService.updateUserDeliveryAddress(
                        deliveryModel,
                        deliveryAddressModel!!.deliveryAddressDocId
                    )
                }
            }
        }
    }


    // 데이터를 읽어와 입력 요소를 채워준다.
    /*fun settingInputData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserDeliveryAddressService.gettingSelectedDeliveryAddress(homeActivity.loginUserDocumentId)
            }
            deliveryAddressModel = work1.await()

            Log.d("test200", "${deliveryAddressModel.deliveryAddressDocId}")

            binding.apply {
                textInputLayoutUserAddressModifyArrivalName.editText?.setText(deliveryAddressModel?.deliveryAddressName ?: "")
                textInputLayoutUserAddressModifyUserName.editText?.setText(deliveryAddressModel?.deliveryAddressReceiverName ?: "")
                textInputLayoutUserAddressModifyPhoneNumber.editText?.setText(deliveryAddressModel?.deliveryAddressPhoneNumber ?: "")
                textViewUserAddressModifyShowAddress.text = deliveryAddressModel.deliveryAddressBasicAddress
                // textViewUserAddressModifyShowPostNumber.text = deliveryAddressModel.deliveryAddressPostNumber
                textInputLayoutUserModifyAddressAddDetailAddress.editText?.setText(deliveryAddressModel?.deliveryAddressDetailAddress ?: "")
            }
        }
    }*/

    fun settingInputData() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                Log.d("test100", "args.docId : ${args.addressDocId}")
                UserDeliveryAddressService.gettingSelectedDeliveryAddress(args.addressDocId)
            }
            deliveryAddressModel = work1.await()

            try {
                // 배송지 정보가 null이 아니거나 값이 설정될 때까지 반복 (2초 제한)
                withTimeout(2000) {  // 2000ms = 2초
                    while (deliveryAddressModel == null) {
                        delay(500)  // 0.5초마다 확인
                    }
                }

                // 배송지 정보가 null이 아니면 데이터 설정
                binding.apply {
                    // 받아온 모델이 기본배송지라면
                    if(deliveryAddressModel!!.deliveryAddressIsDefaultAddress.bool){
                        // 체크박스 숨기기
                        checkboxUserAddressModifyDefaultAddress.visibility = View.GONE
                        // 툴바 메뉴 숨기기
                        val menu = toolBarUserAddressModify.menu
                        val menuItem = menu.findItem(R.id.menu_user_address_manage_delete) // 메뉴 아이템의 ID로 찾기
                        menuItem?.isVisible = false  // 메뉴 숨기기

                    }

                    textInputLayoutUserAddressModifyArrivalName.editText?.setText(
                        deliveryAddressModel?.deliveryAddressName ?: ""
                    )
                    textInputLayoutUserAddressModifyUserName.editText?.setText(
                        deliveryAddressModel?.deliveryAddressReceiverName ?: ""
                    )
                    textInputLayoutUserAddressModifyPhoneNumber.editText?.setText(
                        deliveryAddressModel?.deliveryAddressPhoneNumber ?: ""
                    )
                    textViewUserAddressModifyShowAddress.text =
                        deliveryAddressModel?.deliveryAddressBasicAddress
                    // textViewUserAddressModifyShowPostNumber.text = deliveryAddressModel?.deliveryAddressPostNumber
                    textInputLayoutUserModifyAddressAddDetailAddress.editText?.setText(
                        deliveryAddressModel?.deliveryAddressDetailAddress ?: ""
                    )
                }
            } catch (e: TimeoutCancellationException) {
                // 2초 이내에 데이터가 null인 경우
                Log.e("SettingInputData", "배송지 정보 로딩 실패 (타임아웃): ${e.message}")
            }
        }
    }


}



