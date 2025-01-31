package com.example.frume.login

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserSignUpBinding
import com.example.frume.model.CartModel
import com.example.frume.model.ReviewModel
import com.example.frume.model.UserModel
import com.example.frume.service.CartService
import com.example.frume.service.UserService
import com.example.frume.util.CustomerUserGender
import com.example.frume.util.CustomerUserState
import com.example.frume.util.validateInput
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.time.LocalDate


class UserSignUpFragment : Fragment() {
    private var _binding: FragmentUserSignUpBinding? = null
    private val binding get() = _binding!!

    // 전역 변수 선언 (이 변수에 주소를 저장)
    private val getCustomerUserAddress = mutableMapOf<String, String>()

    // 아이디 중복 확인 검사를 했는지 확인하는 변수
    var isCheckJoinCustomerUserIdExist = false

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLayout() {
        // 툴바 세팅(뒤로 가기)
        settingToolbar()
        // 회원 가입 완료 및 홈화면으로 이동
        onClickSignUpButton()
        onClickIdValidateBtn()

        // setAddressFieldClickListener()
        setAddressFieldClickListener()

        // 전화번호 11자리 제한
        setPhoneNumberLimit()

    }

    // 주소 선택 버튼 클릭 리스너
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAddressFieldClickListener() {
        binding.textFieldUserSignUpAddress.setOnClickListener {
            // AddressActivity로 이동
            val intent = Intent(requireContext(), AddressActivity::class.java)
            getAddressResult.launch(intent)  // 주소 찾기 화면으로 이동
        }
    }

    // sehoon 검사 후 db에 저장
    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertDB() {


        // 아이디 중복 확인을 하지 않았다면
        if (!isCheckJoinCustomerUserIdExist) {
            // AlertDialog를 사용하여 다이얼로그 띄우기
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setTitle("아이디 중복 확인")
                .setMessage("아이디 중복 확인을 해 주세요.")
                .setPositiveButton("확인") { dialog, _ ->
                    // 확인 버튼 클릭 시 동작 (다이얼로그 닫기)
                    dialog.dismiss()
                }

            // 다이얼로그 보여주기
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            return
        }

        // db 저장 코드
        // 아이디
        val customerUserId = binding.textFieldUserSignUpId.editText?.text.toString()
        // 비밀번호
        val customerUserPw = binding.textFieldUserSignUpPw.editText?.text.toString()
        // 이메일
        val customerUserEmail = binding.textFieldUserSignUpEmail.editText?.text.toString()
        // 이름
        val customerUserName = binding.textFieldUserSignUpName.editText?.text.toString()
        // 주민등록번호 앞자리
        val customerUserRRNFirst = binding.textFieldUserSignUpRRNFirst.editText?.text.toString()
        // 주민등록번호 뒷자리
        val customerUserRRNLast = binding.textFieldUserSignUpRRNLast.editText?.text.toString()

        // 주민등록번호 앞 6자리와 뒷 1자리로 나이와 성별 계산
        val (gender, age) = getGenderAndAge(customerUserRRNFirst, customerUserRRNLast[0])
        if (gender == null || age == null) {
            return
        }

        // 나이
        val customerUserAge = age

        // 성별 (CustomerUserGender enum)
        val customerUserGender = when (gender) {
            "남성" -> CustomerUserGender.CUSTOMER_USER_GENDER_MALE
            "여성" -> CustomerUserGender.CUSTOMER_USER_GENDER_FEMALE
            else -> {
                return
            }
        }

        // 휴대폰 번호
        val customerUserPhoneNumber = binding.textFieldUserSignUpPhoneNumber.editText?.text.toString()
        // 회원 주소
       /* val customerUserAddress = mutableMapOf(
            "BasicAddress" to (binding.textFieldUserSignUpAddress.editableText?.toString()?.trim() ?: ""),
            "DetailedAddress" to binding.textFieldUserSignUpDetailAddress.editText?.text.toString().trim(),
            "PostNumber" to (binding.textFieldUserSignUpAddress.editableText?.toString()?.trim() ?: ""),
        )*/

        // 회원 상태 (CustomerUserState enum)
        val customerUserState = CustomerUserState.CUSTOMER_USER_STATE_ACTIVE
        // 등록 날짜
        val customerUserTimeStamp = Timestamp.now()

        // UserModel 객체 생성
        val basicAdderTest = binding.textFieldUserSignUpAddress.text
        val detailAddress = binding.textFieldUserSignUpDetailAddress.editText?.text
        val postNumber = 12345

        val getCustomerUserAddress = mutableMapOf<String, String>()
        // val customerUserAddress: MutableMap<String, String> = mutableMapOf()

        getCustomerUserAddress["BasicAddress"] = "$basicAdderTest"
        getCustomerUserAddress["DetailedAddress"] = "$detailAddress"
        getCustomerUserAddress["PostNumber"] = "$postNumber"

        // 비동기적으로 데이터베이스에 저장하는 부분
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val userModel = UserModel().apply {
                    this.customerUserId = customerUserId
                    this.customerUserPw = customerUserPw
                    this.customerUserEmail = customerUserEmail
                    this.customerUserName = customerUserName
                    this.customerUserRRNFirst = customerUserRRNFirst
                    this.customerUserRRNLast = customerUserRRNLast
                    this.customerUserAge = customerUserAge
                    this.customerUserGender = customerUserGender
                    this.customerUserPhoneNumber = customerUserPhoneNumber
                    this.customerUserAddress = getCustomerUserAddress
                    this.customerUserState = customerUserState
                    this.customerUserTimeStamp = customerUserTimeStamp
                }

                val work1 = async(Dispatchers.IO) {
                    // 실제 데이터 저장 메서드 호출
                    UserService.addCustomerUserData(userModel)
                }

                // 작업이 완료될 때까지 대기
                val userDocId =  work1.await()

                // 회원가입시 장바구니 생성 hj
                val work2 = async(Dispatchers.IO){
                    val cartModel = CartModel()
                    cartModel.customerDocId = userDocId
                    CartService.addMyCart(cartModel)
                }
                // 장바구니 생성 작업 대기
                work2.join()

                // 저장 완료 후 토스트 메시지 표시
                Toast.makeText(requireContext(), "가입이 완료되었습니다. 로그인해 주세요", Toast.LENGTH_LONG).show()

                // 로그인 화면으로 이동
                moveToLogin()

            } catch (e: Exception) {
                // 예외 처리: 데이터베이스 저장 실패 등의 경우
                Toast.makeText(requireContext(), "회원 가입에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 주소 선택을 위한 결과 처리
    @RequiresApi(Build.VERSION_CODES.O)
    private val getAddressResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val address = result.data?.getStringExtra("data")
                // val postNumber = result.data?.getStringExtra("postData")

                // 주소를 TextView에 반영
                binding.textFieldUserSignUpAddress.setText(address)

                // 전역 변수에 주소 값 저장
                getCustomerUserAddress["DetailedAddress"] = binding.textFieldUserSignUpDetailAddress.editText?.text.toString().trim()
                getCustomerUserAddress["BasicAddress"] = address ?: ""
                getCustomerUserAddress["PostNumber"] = address ?: ""

                // 디버깅 로그 출력
                Log.d("test100", "customerUserAddress: $getCustomerUserAddress")
            }
        }

    // 주민등록번호 앞 6자리와 뒷 1자리로 나이와 성별 추출하는 함수
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getGenderAndAge(birthDateStr: String, genderCodeChar: Char): Pair<String?, Int?> {
        if (birthDateStr.length != 6) return Pair(null, null)

        // 성별 판단 (주민등록번호 뒷자리)
        val gender = when (genderCodeChar) {
            '1', '3' -> "남성"
            '2', '4' -> "여성"
            else -> {
                // 잘못된 성별 코드일 경우
                Toast.makeText(requireContext(), "주민번호 뒷자리를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
                return Pair(null, null)
            }
        }

        val century = when (genderCodeChar) {
            '1', '2' -> 1900
            '3', '4' -> 2000
            else -> return Pair(null, null)
        }

        val birthYear = century + birthDateStr.substring(0, 2).toInt()  // 1900년대 또는 2000년대
        val birthMonth = birthDateStr.substring(2, 4).toInt()  // 월
        val birthDay = birthDateStr.substring(4, 6).toInt()  // 일

        // 날짜 유효성 검사
        try {
            val birthDate = LocalDate.of(birthYear, birthMonth, birthDay)

            val today = LocalDate.now()
            val age = today.year - birthDate.year - if (today < birthDate.plusYears((today.year - birthDate.year).toLong())) 1 else 0

            return Pair(gender, age)
        } catch (e: Exception) {
            // 날짜가 유효하지 않으면, 에러 메시지와 함께 null 반환
            Toast.makeText(requireContext(), "생년월일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            return Pair(null, null)
        }
    }

    // sehoon 로그인 화면으로 이동
    private fun moveToLogin() {
        val action = UserSignUpFragmentDirections.actionUserSignupToUserLogin()
        findNavController().navigate(action)
    }


    // sehoon 회원가입 완료 버튼 클릭 메서드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickSignUpButton() {
        binding.buttonUserSignUp.setOnClickListener {
            val emptyInput = emptyEditText() // 값이 비어있는지 확인
            val passwordCheck = integrationPassword() // 비밀번호 통합 확인
            val isCheckBox = isCheckBox()

            if (emptyInput && passwordCheck && isCheckBox) {
                // db에 저장 후 네비게이션 이동 코루틴 사용!
                insertDB()
            } else {
                Toast.makeText(requireContext(), "일치하지 않습니다", Toast.LENGTH_SHORT).show()
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
    // 아이디 중복 확인 버튼 클릭 메서드
    private fun onClickIdValidateBtn() {
        binding.buttonUserSignUpCheck.setOnClickListener {
            val userId = binding.textFieldUserSignUpId.editText?.text.toString()

            // 아이디 유효성 검사
            if (!validateIdInput(userId)) {
                binding.textFieldUserSignUpId.isErrorEnabled = true
                binding.textFieldUserSignUpId.error = "아이디 형식을 다시 확인해 주세요"
                return@setOnClickListener
            }

            // 유효성 검사 통과 후 중복 검사 진행
            CoroutineScope(Dispatchers.Main).launch {
                val isUserIdAvailable = withContext(Dispatchers.IO) {
                    UserService.checkJoinCustomerUserId(userId) // 중복 검사
                }

                // 중복 여부 처리
                if (isUserIdAvailable) {
                    // 중복 없는 경우
                    binding.textFieldUserSignUpId.isErrorEnabled = false
                    isCheckJoinCustomerUserIdExist = true

                    // 중복 없는 경우 버튼 비활성화
                    binding.buttonUserSignUpCheck.isEnabled = false
                    binding.textFieldUserSignUpId.isEnabled = false
                } else {
                    // 중복된 경우
                    binding.textFieldUserSignUpId.isErrorEnabled = true
                    binding.textFieldUserSignUpId.error = "이미 존재하는 아이디입니다."
                    isCheckJoinCustomerUserIdExist = false
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
                textFieldUserSignUpId.validateInput("아이디를 입력해 주세요."),
                textFieldUserSignUpPw.validateInput("비밀번호를 입력해 주세요."),
                textFieldUserSignUpPwCheck.validateInput("비밀번호 확인을 입력해 주세요."),
                textFieldUserSignUpEmail.validateInput("이메일을 입력해 주세요."),
                textFieldUserSignUpName.validateInput("이름을 입력해 주세요."),
                textFieldUserSignUpRRNFirst.validateInput("주민번호 앞 입력해 주세요."),
                textFieldUserSignUpRRNLast.validateInput("주민번호 뒤 입력해 주세요."),
                textFieldUserSignUpPhoneNumber.validateInput("전화번호를 입력해 주세요."),
                textFieldUserSignUpDetailAddress.validateInput("상세주소를 입력해 주세요."),
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

    // 전화번호 11자리 제한을 위한 메서드
    private fun setPhoneNumberLimit() {
        binding.textFieldUserSignUpPhoneNumber.editText?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

           override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val phoneNumber = charSequence.toString()

                // 전화번호가 11자리를 넘지 않도록 제한
                if (phoneNumber.length > 11) {
                    // 11자리 이후로는 더 이상 입력되지 않도록 자르기
                    binding.textFieldUserSignUpPhoneNumber.editText?.setText(phoneNumber.substring(0, 11))
                    binding.textFieldUserSignUpPhoneNumber.editText?.setSelection(11) // 커서 위치를 마지막에 맞추기
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}