package com.example.frume.fragment.payment


import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.activity.HomeActivity
import com.example.frume.data.PaymentDone
import com.example.frume.databinding.FragmentUserPaymentScreenBinding
import com.example.frume.model.CartProductModel
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.model.DeliveryModel
import com.example.frume.model.OrderModel
import com.example.frume.model.OrderProductModel
import com.example.frume.model.ProductModel
import com.example.frume.model.UserModel
import com.example.frume.service.CartProductService
import com.example.frume.service.DeliveryService
import com.example.frume.service.OrderProductService
import com.example.frume.service.OrderService
import com.example.frume.service.ProductService
import com.example.frume.service.UserDeliveryAddressService
import com.example.frume.service.UserService
import com.example.frume.util.DeliveryOption
import com.example.frume.util.OrderPaymentOption
import com.example.frume.util.applyNumberFormat
import com.example.frume.util.convertThreeDigitComma
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// 결제 화면
class UserPaymentScreenFragment : Fragment() {

    private var _binding: FragmentUserPaymentScreenBinding? = null
    private val binding get() = _binding!!
    private val args: UserPaymentScreenFragmentArgs by navArgs()
    lateinit var homeActivity: HomeActivity
    private var paymentOptionState = OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT

    // 스크롤 위치 저장 변수
    private var scrollPotition = 0

    // 배송지를 담을 변수 처음엔 기본배송지를 담을 예정
    var deliveryAddressSpot: DeliveryAddressModel? = null

    // 주문자 정보 담을 변수
    var userModel: UserModel? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_payment_screen,
            container,
            false
        )
        homeActivity = activity as HomeActivity

        // 툴바 뒤로가기
        onClickToolbarNavigationBtn()

        // 카드 선택 -> 카드사 선택 드롭 다운

        setupPaymentMethodButtons()
        setupCheckBoxListeners()

        // 배송지 정보를 받아오는 메서드 호출
        getReceiverData()
        // 배송지 정보를 토대로 배송지 정보를 세팅하는 메서드 호출
        settingReceiverInfo()
        // 보유 적립금 표시 메서드 호출
        settingViewMyReward()
        // 상품 가격 표시 메서드 호출
        settingViewProductsPrice()
        // 배송비 표시 메서드 호출
        settingViewDeliveryCost()
        // 적립금 사용 editText 설정 메서드 호출
        settingTextInputLayoutUserPaymentSaving()
        // 적립금 전액사용 체크 리스너 메서드 호출
        checkUseRewardAll()
        // 총 결제 금액 표시 메서드 호출
        settingViewTotalCost()
        // 주문 완료 버튼 클릭 시 처리 메서드 호출
        onClickButtonButtonUserCartOrder()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickToolbarNavigationBtn()  // 툴바 내비게이션 버튼 클릭 리스너 설정
        onClickPaymentDeliverySpotChange()  // 배송지 변경 버튼 클릭 리스너 설정
        setupDeliveryWayRadioButtons()  // 배송 방식 라디오 버튼 설정
        // 주문자 정보 가져와 view에 적용하는 메서드 호출
        gettingUserInfo()
        onClickTextView()
        setAccountAndCard(1)
    }

    private fun onClickTextView() {
        binding.scrollView.post {
            binding.scrollView.scrollTo(0, scrollPotition)
        }
        // 배송 안내
        binding.textViewUserPaymentGuide1.setOnClickListener {
            scrollPotition = binding.scrollView.scrollY
            val action =
                UserPaymentScreenFragmentDirections.actionUserPaymentScreenToUserPaymentWebView(0)
            findNavController().navigate(action)
        }
        // 교환 안내
        binding.textViewUserPaymentGuide2.setOnClickListener {
            scrollPotition = binding.scrollView.scrollY
            val action =
                UserPaymentScreenFragmentDirections.actionUserPaymentScreenToUserPaymentWebView(1)
            findNavController().navigate(action)
        }
    }

    // 툴바 내비게이션 버튼 클릭 리스너
    private fun onClickToolbarNavigationBtn() {
        binding.toolbarUserPaymentScreen.setNavigationOnClickListener {
            // 내비게이션 아이콘을 클릭하면 이전 화면으로 돌아감
            findNavController().navigateUp()
        }
    }

    // 카드 드롭다운 리스너
    private fun setupPaymentCardDropdown() {

        val autoCompletePaymentCardTextView = binding.autoCompleteTextViewUserPaymentCard

        // 드롭다운 데이터 정의
        val cardTypes = listOf("선택", "삼성", "신한", "국민", "롯데", "현대", "하나", "NH", "우리", "카카오뱅크", "비씨")

        // ArrayAdapter 생성 (autoCompletePaymentCardTextView에 데이터 연결)
        val adapterCardType = ArrayAdapter(
            homeActivity,
            android.R.layout.simple_dropdown_item_1line,
            cardTypes
        )

        // autoCompletePaymentCardTextView에 연결
        autoCompletePaymentCardTextView.setAdapter(adapterCardType)

        // autoCompletePaymentCardTextView 항목 선택 이벤트 리스너 설정
        autoCompletePaymentCardTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCardType = parent.getItemAtPosition(position).toString()
        }
    }

    private fun setAccountAndCard(type: Int) {
        when (type) {
            1 -> {
                with(binding) {
                    textInputLayoutUserPaymentCard.visibility = View.VISIBLE
                    textViewUserPaymentCard.visibility = View.VISIBLE
                    textViewUserPaymentStar.visibility = View.VISIBLE

                    textViewUserPaymentCard.text = "계좌 번호"
                    textInputLayoutUserPaymentCard.hint = "계좌 번호"
                    textInputLayoutUserPaymentCard.editText?.setText("푸르미(멋사) 20250106-20250206")
                    textInputLayoutUserPaymentCard.isEnabled = false
                }
            }

            2 -> {
                with(binding) {
                    textInputLayoutUserPaymentCard.visibility = View.VISIBLE
                    textViewUserPaymentCard.visibility = View.VISIBLE
                    textViewUserPaymentStar.visibility = View.VISIBLE

                    textViewUserPaymentCard.text = "카드 선택"
                    textInputLayoutUserPaymentCard.hint = "카드"
                    textInputLayoutUserPaymentCard.editText?.setText("선택")
                    textInputLayoutUserPaymentCard.isEnabled = true
                    setupPaymentCardDropdown()
                }
            }

            else -> {
                with(binding) {
                    textInputLayoutUserPaymentCard.visibility = View.GONE
                    textViewUserPaymentCard.visibility = View.GONE
                    textViewUserPaymentStar.visibility = View.GONE
                }
            }
        }
    }

    // 결제 방식 버튼을 단일 선택만 가능하도록 하는 메서드
    private fun setupPaymentMethodButtons() {

        // 버튼을 리스트로 묶어 관리
        val buttons = listOf(
            binding.buttonUserPaymentPaymentMethodAccount,
            binding.buttonUserPaymentPaymentMethodCard,
            binding.buttonUserPaymentPaymentMethodKakaoPay,
            binding.buttonUserPaymentPaymentMethodNaverPay
        )

        // 각 버튼에 대한 클릭 이벤트
        buttons.forEach { button ->
            button.setOnClickListener {
                // 모든 버튼을 비활성화 스타일(AppButtonCancel)로 변경
                buttons.forEach { btn ->
                    btn.setBackgroundResource(R.drawable.background_gray100) // 비활성화 배경
                    //btn.setTextAppearance(R.style.AppButtonCancel) // 비활성화 텍스트 스타일
                }

                // 클릭된 버튼을 활성화 스타일(CustomButtonStyle)로 변경
                button.setBackgroundResource(R.drawable.background_green100) // 활성화 배경
                //button.setTextAppearance(R.style.CustomButtonStyle) // 활성화 텍스트 스타일


                // 각 버튼에 따른 추가 동작 처리
                when (button.id) {
                    R.id.buttonUserPaymentPaymentMethodAccount -> {
                        // 계좌이체 버튼 선택 시 동작
                        setAccountAndCard(1)
                        paymentOptionState = OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT
                    }

                    R.id.buttonUserPaymentPaymentMethodCard -> {
                        // 신용카드 버튼 선택 시 동작
                        setAccountAndCard(2)
                        paymentOptionState = OrderPaymentOption.ORDER_PAYMENT_OPTION_CARD

                    }

                    R.id.buttonUserPaymentPaymentMethodKakaoPay -> {
                        // 카카오페이 버튼 선택 시 동작
                        setAccountAndCard(3)
                        paymentOptionState = OrderPaymentOption.ORDER_PAYMENT_OPTION_KAKAO_PAY

                    }

                    R.id.buttonUserPaymentPaymentMethodNaverPay -> {
                        // 네이버페이 버튼 선택 시 동작
                        setAccountAndCard(4)
                        paymentOptionState = OrderPaymentOption.ORDER_PAYMENT_OPTION_NAVER_PAY
                    }
                }
            }
        }
    }

    private fun setupCheckBoxListeners() {
        val topCheckBox = binding.checkboxUserPaymentAgreeAll
        val otherCheckBoxes = listOf(
            binding.checkboxUserPaymentAgree1,
            binding.checkboxUserPaymentAgree2
        )
        val orderButton = binding.buttonUserCartOrder

        // 최상단 체크박스 상태 변경 시 다른 체크박스 모두 변경
        topCheckBox.setOnCheckedChangeListener { _, isChecked ->
            otherCheckBoxes.forEach { it.isChecked = isChecked }
        }

        // 하위 체크박스 상태 변경 시 최상단 체크박스 상태 업데이트
        otherCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                // 모든 하위 체크박스가 선택된 경우만 최상단 체크박스를 선택
                topCheckBox.isChecked = otherCheckBoxes.all { it.isChecked }
                updateOrderButtonState(orderButton, otherCheckBoxes)
            }
        }
    }

    // 결제 버튼 활성화
    private fun updateOrderButtonState(button: View, checkBoxes: List<View>) {
        // 모든 체크박스가 선택되었을 때만 버튼 활성화
        button.isEnabled =
            checkBoxes.all { (it as? androidx.appcompat.widget.AppCompatCheckBox)?.isChecked == true }
    }


    // 배송지 변경 아이콘 클릭 시 UserAddressManageFragment로 이동하는 메서드
    private fun onClickPaymentDeliverySpotChange() {
        binding.imageViewUserPaymentAddressModify.setOnClickListener {
            // 배송지 수정 버튼 클릭 시, 배송지 관리 화면으로 이동
            val action =
                UserPaymentScreenFragmentDirections.actionUserPaymentScreenToUserCartChoiceDeliverAddress(
                    args.fromWhere,
                    args.productDocIdDirectPurchase,
                    args.dueDateDirectPurchase,
                    args.deliverySubscribeStateDirectPurchase,
                    args.productCountDirectPurchase
                )
            findNavController().navigate(action)
        }
    }

    // 라디오 버튼 설정 및 선택 처리
    private fun setupDeliveryWayRadioButtons() {
        // 기본적으로 '문 앞 배송'이 선택된 상태로 설정
        binding.buttonUserPaymentDeliveryMethodDoor.setBackgroundResource(R.drawable.background_green100)

        // 각 라디오 버튼에 클릭 리스너를 설정하여 버튼 선택 상태를 처리
        binding.buttonUserPaymentDeliveryMethodDoor.setOnClickListener {
            // '문 앞 배송' 버튼 클릭 시 선택된 상태로 처리
            toggleButtonSelection(binding.buttonUserPaymentDeliveryMethodDoor)
        }
        binding.buttonUserPaymentDeliveryMethodBox.setOnClickListener {
            // '택배함' 버튼 클릭 시 선택된 상태로 처리
            toggleButtonSelection(binding.buttonUserPaymentDeliveryMethodBox)
        }
        binding.buttonUserPaymentDeliveryMethodOffice.setOnClickListener {
            // '경비실' 버튼 클릭 시 선택된 상태로 처리
            toggleButtonSelection(binding.buttonUserPaymentDeliveryMethodOffice)
        }
    }

    // 선택된 버튼의 배경을 변경하고 나머지 버튼들은 기본 배경으로 설정
    private fun toggleButtonSelection(selectedButton: RadioButton) {
        // 모든 버튼을 기본 배경으로 리셋
        listOf(
            binding.buttonUserPaymentDeliveryMethodDoor,
            binding.buttonUserPaymentDeliveryMethodBox,
            binding.buttonUserPaymentDeliveryMethodOffice
        ).forEach { button ->
            // 모든 버튼에 대해 기본 배경을 설정 (회색 배경)
            button.setBackgroundResource(R.drawable.background_gray100)
        }

        // 선택된 버튼에만 초록색 배경을 적용
        selectedButton.setBackgroundResource(R.drawable.background_green100)
    }

    // 주문자 정보 가져와 view에 적용하는 메서드
    fun gettingUserInfo() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                UserService.getUserInfo(homeActivity.loginUserDocumentId)
            }

            val timeoutResult = withTimeoutOrNull(2000) {  // 2초 동안 대기
                while (userModel == null) {
                    userModel = work1.await().getOrNull(0)
                    delay(100)  // 너무 빠른 반복을 방지
                }
                true  // 성공적으로 값을 가져온 경우
            }

            if (timeoutResult == null) {
                // 2초 내에 userModel을 가져오지 못한 경우 예외 처리
                binding.apply {
                    textViewUserPaymentUserName.text = "주문자 정보를 불러오지 못했습니다."
                    textViewPaymentUserPhoneNumber.text = "주문자 정보를 불러오지 못했습니다."
                }
            } else {
                // 정상적으로 userModel이 설정된 경우 UI 업데이트
                binding.apply {
                    textViewUserPaymentUserName.text = "주문자 이름 : ${userModel?.customerUserName}"
                    textViewPaymentUserPhoneNumber.text =
                        "번호 : ${userModel?.customerUserPhoneNumber}"
                }
            }
        }
    }

    // arg 값에 따라 배송지를 가져와 spot에 넣어준다. args == null -> 기본 배송지
    // 배송지를 유저정보에서 가져오지 않고, 배송지 DB에서 기본배송지를 가져오는 것으로 수정 hj 받는사람 이름도 DeliveryAddres Model 에 추가함
    private fun getReceiverData() {
        // 기본 배송지를 가져온다
        if (args.deliveryAddressDocId == null) {
            Log.d("test100", "args.deliveryAddressDocId == null ")
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    UserDeliveryAddressService.gettingDefaultDeliveryAddress(homeActivity.loginUserDocumentId)
                }
                deliveryAddressSpot = work1.await()
            }
        } else {
            // 선택된 배송지를 가져온다
            Log.d("test100", "not None -> args.deliveryAddressDocId: ${args.deliveryAddressDocId}")
            CoroutineScope(Dispatchers.Main).launch {
                val work2 = async(Dispatchers.IO) {
                    UserDeliveryAddressService.gettingSelectedDeliveryAddress(args.deliveryAddressDocId!!)
                }
                deliveryAddressSpot = work2.await()
            }
        }
    }

    // 아마 mvvm liveData쓰면 코드 바뀔듯
    // 배송지 정보를 토대로 배송지 정보를 입력한다.
    private fun settingReceiverInfo() {
        binding.apply {
            // CoroutineScope 사용
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // 배송지 정보가 null이 아니거나 값이 설정될 때까지 반복 (2초 제한)
                    withTimeout(2000) {  // 2000ms = 2초
                        while (deliveryAddressSpot == null) {
                            delay(500)  // 0.5초마다 확인
                        }
                    }

                    // 배송지 정보가 null이 아니면 UI 업데이트
                    deliveryAddressSpot?.let {
                        val deliveryAddressName = it.deliveryAddressName
                        val receiverName = it.deliveryAddressReceiverName
                        val basicAddress = it.deliveryAddressBasicAddress
                        val detailAddress = it.deliveryAddressDetailAddress
                        val phoneNumber = it.deliveryAddressPhoneNumber

                        // UI에 배송지 정보 입력
                        // 배송지명
                        viewUserPaymentAddressModifyAddressName.text = deliveryAddressName
                        // 받는사람 이름
                        viewUserPaymentAddressModifyUserName.text = receiverName
                        // 주소
                        viewUserPaymentAddressModifyUserAddress.text =
                            "${basicAddress}  ${detailAddress}"
                        // 받는사람 전화번호
                        viewUserPaymentAddressModifyUserPhoneNumber.text = phoneNumber
                    }
                } catch (e: TimeoutCancellationException) {
                    // 2초 이내에 배송지 정보가 로드되지 않으면 타임아웃 처리
                    // 여기서 타임아웃 후 처리를 할 수 있음 (예: 로딩 실패 메시지 표시)
                    viewUserPaymentAddressModifyAddressName.text = "배송지 로드 실패"
                    viewUserPaymentAddressModifyUserName.text = "배송지 로드 실패"
                    viewUserPaymentAddressModifyUserAddress.text = "배송지 로드 실패"
                    viewUserPaymentAddressModifyUserPhoneNumber.text = "배송지 로드 실패"
                }
            }
        }
    }

    // 기본 배송지로 설정이 돼 있다면
    fun changeBasicDeliverAddress() {
        CoroutineScope(Dispatchers.Main).launch {

            // 현재 기본배송지를 일반 배송지로 바꾼다.
            val work1 = async(Dispatchers.IO) {
                UserDeliveryAddressService.changeDefaultStateToFalse(
                    homeActivity.loginUserDocumentId,
                    deliveryAddressSpot!!.deliveryAddressDocId
                )
            }
            work1.join()

            // 현재 설정된 배송지를 기본 배송지로 변경한다.
            val work2 = async(Dispatchers.IO) {
                UserDeliveryAddressService.changeDefaultStateToTrue(
                    homeActivity.loginUserDocumentId,
                    deliveryAddressSpot!!.deliveryAddressDocId
                )
            }
            work2.join()
        }

    }

    // 적립금 전액 사용 체크
    fun checkUseRewardAll() {
        CoroutineScope(Dispatchers.Main).launch {
            while (userModel == null) {
                delay(500) // 500ms 대기 후 다시 확인
            }
            val userReward = userModel?.customerUserReward ?: 0

            binding.checkboxUserPaymentUseSavingAll.setOnClickListener {
                binding.textInputLayoutUserPaymentSaving.editText?.setText(
                    if (binding.checkboxUserPaymentUseSavingAll.isChecked) userReward.toString() else "0"
                )
                // 적립금 사용액 표시 메서드 호출
                settingViewReward()
                // 총 결제 금액 표시
                settingViewTotalCost()
            }
        }
    }


    // 적립금 사용 editText 설정
    fun settingTextInputLayoutUserPaymentSaving() {
        binding.apply {
            textInputLayoutUserPaymentSaving.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // 상품 가격
                    val productCost =
                        textViewProductTotalPrice.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 0
                    val userReward = userModel?.customerUserReward ?: -1 // 보유 리워드 (없으면 0)
                    val inputText = s.toString()

                    // 입력값이 숫자인지 확인 후 비교
                    val inputValue = inputText.toIntOrNull() ?: 0

                    // 상품 가격의 20% 계산 (한도)
                    val maxRewardLimit = (productCost * 0.2).toInt()
                    val rewardLimit = minOf(userReward, maxRewardLimit) // 적립금 사용 한도

                    // 0으로 시작하지 못하도록 방지 0123원은 없으니 문자길이가 2이상인데 앞글자가 0이면 0을 없애줌
                    if (inputText.length > 1 && inputText[0] == '0') {
                        val newText = inputText.toIntOrNull()?.toString() ?: "0"
                        textInputLayoutUserPaymentSaving.editText?.setText(newText)
                        // 커서를 뒤로 옮기기
                        textInputLayoutUserPaymentSaving.editText?.setSelection(newText.length)
                    }

                    if (inputText.length == 0) {
                        val newText = inputText.toIntOrNull()?.toString() ?: "0"
                        textInputLayoutUserPaymentSaving.editText?.setText(newText)
                        // 커서를 뒤로 옮기기
                        textInputLayoutUserPaymentSaving.editText?.setSelection(newText.length)
                    }

                    if (inputValue > rewardLimit) {
                        // 보유 리워드보다 크면 자동으로 보유 리워드 값으로 설정
                        textInputLayoutUserPaymentSaving.editText?.setText(rewardLimit.toString())
                        // 커서를 뒤로 옮기기
                        textInputLayoutUserPaymentSaving.editText?.setSelection(rewardLimit.toString().length)
                    }


                    if (inputText.isNotEmpty()) {
                        // 값을 변경할때 값과 보유 리워드와 같으면 발생하는 리스너
                        if (inputText.toInt() >= rewardLimit) {
                            checkboxUserPaymentUseSavingAll.isChecked = true
                        } else {
                            // 전체 체크 상태에서 값을 변경할때 발생하는 리스너
                            checkboxUserPaymentUseSavingAll.isChecked = false

                        }

                        // 적립금 사용금 표시 메서드 호출
                        settingViewReward()
                        // 총 결제 금액 표시
                        settingViewTotalCost()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    // 배송비 설정
    fun settingViewDeliveryCost() {
        // 장바구니 루트로 온 경우
        if (args.fromWhere == "Cart") {
            // 장바구니에 있는 물건들 총 가격 구매 여부가 true인것들 합계 구하기
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    CartProductService.gettingMyCartProductCheckedItems(homeActivity.userCartDocId)
                }
                val checkedList = work1.await()
                var sumPrice = 0
                checkedList.forEach { sumPrice += it.cartProductPrice }

                if (sumPrice >= 50000) {
                    binding.textViewUserPaymentDeliveryCharge.text = "0원"
                } else {
                    binding.textViewUserPaymentDeliveryCharge.text = "3,000원"
                }
            }
        } else {
            // 상품에서 바로 구매한 경우
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    ProductService.gettingProductOneByDocId(args.productDocIdDirectPurchase!!)
                }
                val product = work1.await()
                val productCost = product.productPrice * args.productCountDirectPurchase
                if (productCost >= 50000) {
                    binding.textViewUserPaymentDeliveryCharge.text = "0원"
                } else {
                    binding.textViewUserPaymentDeliveryCharge.applyNumberFormat(3000)
                }
            }
        }
    }

    // 보유 적립금 설정
    fun settingViewMyReward() {
        CoroutineScope(Dispatchers.Main).launch {
            while (userModel == null) {
                delay(500) // 500ms 대기 후 다시 확인
            }

            // userModel이 null이 아니면 UI 업데이트
            binding.apply {
                textViewUserPaymentRequestPresentSaving.text =
                    "보유 적립금 : ${(userModel?.customerUserReward ?: -1).convertThreeDigitComma()}"
            }
        }
    }


    // 적립금 사용금 표시
    fun settingViewReward() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(0)
            binding.apply {
                textViewUserPaymentTotalSavingInfo.text =
                    textInputLayoutUserPaymentSaving.editText?.text.toString().toInt()
                        .convertThreeDigitComma()
            }
        }
    }

    // 상품 가격 표시
    fun settingViewProductsPrice() {
        when (args.fromWhere) {
            "Cart" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        CartProductService.gettingMyCartProductCheckedItems(homeActivity.userCartDocId)
                    }
                    val checkedList = work1.await()
                    var sumPrice = 0
                    checkedList.forEach {
                        sumPrice += it.cartProductPrice
                    }

                    binding.apply {
                        textViewProductTotalPrice.text = "${sumPrice.convertThreeDigitComma()}"
                    }
                }
            }

            else -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        ProductService.gettingProductOneByDocId(args.productDocIdDirectPurchase!!)
                    }
                    val productModel = work1.await()
                    val sumPrice = productModel.productPrice * args.productCountDirectPurchase

                    binding.apply {
                        textViewProductTotalPrice.text = "${sumPrice.convertThreeDigitComma()}"
                    }
                }
            }
        }
    }

    // 총 결제 금액 표시
    fun settingViewTotalCost() {
        binding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val productPrice = withTimeoutOrNull(2000) {
                    while (true) {
                        val text  =
                        textViewProductTotalPrice.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 0
                        val value = text

                        if (value != null && value > 0) return@withTimeoutOrNull value
                        delay(100) // 100ms 간격으로 다시 확인
                    }
                } ?: 0 // 2초 후에도 값이 없으면 0으로 처리

                val work2 = async(Dispatchers.Default) {
                    textViewUserPaymentTotalSavingInfo.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 0
                }

                val work3 = async(Dispatchers.Default) {
                    textViewUserPaymentDeliveryCharge.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 0

                }

                val usedReward = work2.await()
                val shipmentCost = work3.await()

                val totalCost = productPrice as Int + shipmentCost  - usedReward

                // 총 결제 금액 UI 업데이트
                textViewUserPaymentTotalPayment.text = totalCost.convertThreeDigitComma()
            }
        }
    }






    // 주문 완료 버튼 클릭 시 처리
    fun onClickButtonButtonUserCartOrder() {
        binding.buttonUserCartOrder.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                // 주문 성공 여부 변수
                var result = true
                // 1. 배송 정보 추가
                val deliveryDocId = addDelivery()

                // 2. 주문 추가 (배송 문서 ID 전달)
                val orderDocId = addOrder(deliveryDocId)

                // 장바구니에서 주문화면으로 도달한 경우
                result = if (args.fromWhere == "Cart") {
                    // 3. 주문 상품 추가 (주문 문서 ID 전달)
                    addOrderProduct(orderDocId)
                } else {
                    // 상품 정보에서 바로 구매하러 온 경우
                    addOrderProductWhenDirectPurchase(orderDocId)
                }
                // 4. 결과에 따라 주소 변경
                if (result) {
                    if (binding.checkboxUserPaymentDefaultAddress.isChecked) {
                        // 기본 배송지로 설정을 체크했다면
                        changeBasicDeliverAddress()
                    }
                    // 5. 주문 후 장바구니에서 구매했던 품목은 삭제처리하기
                    deleteCartProductAfterPurchase()
                    // 사용자 적립금 차감 메서드 호출
                    updateUserReward()
                    moveToPaymentDone()
                }
            }
        }
    }


    // 배송 문서 생성
    suspend fun addDelivery(): String {
        return withContext(Dispatchers.IO) {
            // IO 디스패처에서 백그라운드 작업 수행
            val result: String
            val addDeliveryModel = DeliveryModel()

            withContext(Dispatchers.Main) {
                binding.apply {
                    // 배송 방식 받아오기
                    val deliverOption =
                        when (radioGroupUserPaymentDeliveryMethod.checkedRadioButtonId) {
                            R.id.buttonUserPaymentDeliveryMethodDoor -> DeliveryOption.DOOR_DELIVERY // 문앞 배송
                            R.id.buttonUserPaymentDeliveryMethodBox -> DeliveryOption.PARCEL_LOCKER // 택배 보관함
                            R.id.buttonUserPaymentDeliveryMethodOffice -> DeliveryOption.SECURITY_OFFICE // 보안실 배송
                            else -> DeliveryOption.DOOR_DELIVERY // 기본값: 문앞 배송
                        }

                    // 배송지 문서 ID 설정
                    addDeliveryModel.deliveryAddressDocId =
                        deliveryAddressSpot?.deliveryAddressDocId ?: "null"

                    // 배송 방식 설정
                    addDeliveryModel.deliveryOption = deliverOption

                    // 기타 요청 사항 입력값 받기
                    addDeliveryModel.deliveryEtc =
                        textInputLayoutUserPaymentRequest.editText?.text.toString()
                }
            }

            // 배송 서비스 호출
            result = DeliveryService.addUserDelivery(addDeliveryModel)

            // 배송 DocId 리턴
            result
        }
    }


    // 주문 넣기 메서드
    suspend fun addOrder(addDeliverDocId: String): String {
        return withContext(Dispatchers.IO) {
            // IO 디스패처에서 백그라운드 작업 수행
            val result: String
            val addOrderModel = OrderModel()

            withContext(Dispatchers.Main) {
                binding.apply {

                    addOrderModel.orderCustomerDocId = homeActivity.loginUserDocumentId

                    // 배송 DocID
                    addOrderModel.deliverDocId = addDeliverDocId

                    // 배송비
                    addOrderModel.orderDeliveryCost =
                        textViewUserPaymentDeliveryCharge.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 0
                    // 적립금 사용액
                    addOrderModel.usedReward =
                        if (textViewUserPaymentTotalSavingInfo.text.toString() == "") {
                            0
                        } else {
                            textViewUserPaymentTotalSavingInfo.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 0
                        }
                    // 결제 방식
                    addOrderModel.orderPaymentOption = when (paymentOptionState) {
                        OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT -> OrderPaymentOption.ORDER_PAYMENT_OPTION_ACCOUNT
                        OrderPaymentOption.ORDER_PAYMENT_OPTION_CARD -> OrderPaymentOption.ORDER_PAYMENT_OPTION_CARD
                        OrderPaymentOption.ORDER_PAYMENT_OPTION_KAKAO_PAY -> OrderPaymentOption.ORDER_PAYMENT_OPTION_KAKAO_PAY
                        OrderPaymentOption.ORDER_PAYMENT_OPTION_NAVER_PAY -> OrderPaymentOption.ORDER_PAYMENT_OPTION_NAVER_PAY
                    }

                    // 주문 추가 서비스 호출
                    result = OrderService.addMyOrder(addOrderModel)
                }
            }
            result // orderDocId 문자열 반환
        }
    }

    suspend fun addOrderProduct(addOrderId: String): Boolean {
        val orderProductList = mutableListOf<OrderProductModel>()
        val cartProductList =
            CartProductService.gettingMyCartProductCheckedItems(homeActivity.userCartDocId)

        binding.apply {
            cartProductList.forEach {
                val orderProductModel = OrderProductModel()
                // order ID
                orderProductModel.orderId = addOrderId
                // 주문상품 이름
                orderProductModel.orderProductName = it.cartProductName
                // 주문상품 수량
                orderProductModel.orderProductCount = it.cartItemProductQuantity
                // 주문상품 예정일
                orderProductModel.orderDeliveryDueDate = it.cartItemDeliveryDueDate
                // 주문상품 문서 ID 수정 hj orderProductDocId -> productDocId 수정함
                orderProductModel.productDocId = it.cartItemProductDocId
                // 주문상품 이미지 받아오기
                val productModel: ProductModel =
                    ProductService.gettingProductOneByDocId(it.cartItemProductDocId)
                val imgPath = productModel.productImages[0]
                // 주문상품 이미지 설정
                orderProductModel.orderProductImagePath = imgPath
                // 주문상품 단가
                orderProductModel.orderProductPrice = it.cartProductUnitPrice
                // 주문상품 가격
                orderProductModel.orderProductTotalPrice = it.cartProductPrice
                // 구독상품
                orderProductModel.deliveryIsSubscribed = it.cartItemIsSubscribed
                orderProductList.add(orderProductModel)
            }

            // 주문 상품 추가 서비스 호출
            val result = OrderProductService.addOrderProduct(addOrderId, orderProductList)

            // 반환 값에 따라 처리 (예: 성공시 true, 실패시 false)
            return result // 이 부분은 OrderProductService의 반환 값을 기반으로 true/false 반환
        }
    }

    // 주문 후 장바구니에서 구매품목 삭제
    fun deleteCartProductAfterPurchase() {
        Log.d("test100", "UserPaymentScreenFragment -> deleteCartProductAfterPurchase()")

        CoroutineScope(Dispatchers.IO).launch {
            val list: MutableList<CartProductModel> = withContext(Dispatchers.IO) {
                CartProductService.gettingMyCartProductCheckedItems(homeActivity.userCartDocId)
            }

            if (list.isEmpty()) {
                Log.d("test100", "UserPaymentScreenFragment -> checked == true 인 품목 없음.")
                return@launch
            }

            val deleteList = mutableListOf<String>()
            list.forEach {
                deleteList.add(it.cartProductDocId)
            }

            // 구매 품목 장바구니에서 제거
            CartProductService.deleteCartProducts(homeActivity.userCartDocId, deleteList)
        }
    }

    // 사용자 적립금 관련 메서드
    fun updateUserReward() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val userModel = UserService.getUserInfo(homeActivity.loginUserDocumentId)[0]
                var usedReward = 0
                var leftReward = userModel.customerUserReward

                val rewardInputText =
                    binding.textInputLayoutUserPaymentSaving.editText?.text?.toString()
                if (!rewardInputText.isNullOrEmpty()) {
                    usedReward = rewardInputText.toInt()
                    leftReward -= usedReward
                }

                // 사용 적립금 차감
                userModel.customerUserReward = leftReward
                UserService.updateUserData(userModel)

                // 물건 가격의 1퍼센트 적립
                val productCost = binding.textViewProductTotalPrice.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 0
                val getReward = (productCost * 0.01).toInt()

                userModel.customerUserReward += getReward
                UserService.updateUserData(userModel)
            }
        }
    }

    private fun moveToPaymentDone() {

        val totalPrice = binding.textViewUserPaymentTotalPayment.text.toString().filter { it.isDigit() }.toInt()
        val payment = paymentOptionState.str
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26 이상: java.time.LocalDate 사용
            val today = LocalDate.now()
            today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
            // API 24~25: java.util.Date & SimpleDateFormat 사용
            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formatter.format(date)
        }

        val paymentDone = PaymentDone(payment, totalPrice, date)
        val action = UserPaymentScreenFragmentDirections.actionUserPaymentScreenToUserPaymentDoneFragment(paymentDone)

        findNavController().navigate(action)
    }

    suspend fun addOrderProductWhenDirectPurchase(addOrderId: String): Boolean {
        // 상품 정보 가져오기 (비동기)
        val product = withContext(Dispatchers.IO) {
            ProductService.gettingProductOneByDocId(args.productDocIdDirectPurchase ?: "")
        }

        val dueDateTimeStamp = convertToTimestamp(args.dueDateDirectPurchase ?: "2024-01-01")

        // 주문상품 모델 설정
        val orderProductModel = OrderProductModel().apply {
            // order ID
            orderId = addOrderId
            // 주문상품 이름
            orderProductName = product.productName
            // 주문상품 수량
            orderProductCount = args.productCountDirectPurchase
            // 주문상품 예정일
            orderDeliveryDueDate = dueDateTimeStamp
            // 주문상품 문서 ID 수정 hj orderProductDocId -> productDocId 수정함
            productDocId = product.productDocId
            // 주문상품 이미지 받아오기
            val imgPath = product.productImages[0]
            // 주문상품 이미지 설정
            orderProductImagePath = imgPath
            // 주문상품 단가
            orderProductPrice = product.productPrice
            // 주문상품 가격
            orderProductTotalPrice = product.productPrice * args.productCountDirectPurchase
            // 구독상품
            deliveryIsSubscribed = args.deliverySubscribeStateDirectPurchase
        }

        // 주문 상품 목록에 추가
        val orderProductList = mutableListOf(orderProductModel)

        // 주문 상품 추가 서비스 호출
        val result = OrderProductService.addOrderProduct(addOrderId, orderProductList)

        // 결과 반환
        return result // 이 부분은 OrderProductService의 반환 값을 기반으로 true/false 반환
    }


    // 날짜 타입 변경 String-> Timestamp
    // DB에 넣을때 오후 12시로 넣기위해, kst(한국시간 오후 12시) -> utc(세계기준시간 으로 변환)
    // 시간 기준이 달라서 31일을 저장해도 30일로 저장되는 문제를 해결
    // 아마 00시면 분단위로 짤려서 날짜가 조정됨 그래서 안전하게 오후 12시로 저장함
    fun convertToTimestamp(dueDate: String): Timestamp {
        // 날짜 포맷터 생성
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        dateFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        return try {
            // 문자열을 Date 객체로 변환
            val parsedDate = dateFormatter.parse(dueDate)

            if (parsedDate != null) {
                // 시간을 오후 12시(정오)로 설정
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                calendar.time = parsedDate
                calendar.set(Calendar.HOUR_OF_DAY, 12)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                // 변경된 시간을 UTC로 변환하여 Timestamp 객체 생성
                val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utcCalendar.timeInMillis = calendar.timeInMillis
                Timestamp(utcCalendar.time)
            } else {
                Timestamp.now()  // 날짜가 잘못된 경우 현재 시간을 반환
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Timestamp.now()  // 예외 발생 시 현재 시간 반환
        }
    }

}
