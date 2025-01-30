package com.example.frume.fragment.user_fragment.user_payment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.RadioButton
import androidx.core.widget.addTextChangedListener


import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserPaymentScreenBinding
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.model.DeliveryModel
import com.example.frume.model.OrderModel
import com.example.frume.model.OrderProductModel
import com.example.frume.model.UserModel
import com.example.frume.repository.OrderRepository
import com.example.frume.service.CartProductService
import com.example.frume.service.OrderService
import com.example.frume.service.UserDeliveryAddressService
import com.example.frume.service.UserService
import com.example.frume.util.DeliveryOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

// 결제 화면
class UserPaymentScreenFragment : Fragment() {


    private var _binding: FragmentUserPaymentScreenBinding? = null
    private val binding get() = _binding!!
    private val args: UserPaymentScreenFragmentArgs by navArgs()
    lateinit var homeActivity: HomeActivity

    // 배송지를 담을 변수 처음엔 기본배송지를 담을 예정
    var deliveryAddressSpot: DeliveryAddressModel? = null

    // 주문자 정보 담을 변수
    var userModel: UserModel? = null

    // 배송 정보 담을 변수
    var deliveryModel: DeliveryModel? = null

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
        setupPaymentCardDropdown()

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
        settingViewDeliverCost()
        // 적립금 사용 editText 설정 메서드 호출
        settingTextInputLayoutUserPaymentSaving()
        // 적립금 전액사용 체크 리스너 메서드 호출
        checkUseRewardAll()
        // 총 결제 금액 표시 메서드 호출
        settingViewTotalCost()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickToolbarNavigationBtn()  // 툴바 내비게이션 버튼 클릭 리스너 설정
        onClickPaymentDeliverySpotChange()  // 배송지 변경 버튼 클릭 리스너 설정
        setupDeliveryWayRadioButtons()  // 배송 방식 라디오 버튼 설정
        // 주문자 정보 가져와 view에 적용하는 메서드 호출
        gettingUserInfo()
        // sehoonTest()
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
            // 선택된 항목 처리
            Toast.makeText(requireContext(), "선택된 상태 : $selectedCardType", Toast.LENGTH_SHORT)
                .show()
        }
    }


    // sehoon test
//        private fun sehoonTest() {
//            // 배달비 x
//            var totalPrice = args.userDocId
//            lifecycleScope.launch(Dispatchers.IO) {
//                withContext(Dispatchers.Main) {
//
//                }
//            }
//            binding.textViewProductTotalPrice.applyNumberFormat(totalPrice)
//            if (args.productPriceMethod > 50000) {
//                binding.buttonUserCartOrder.text = totalPrice.convertThreeDigitComma()
//                binding.textViewUserPaymentDeliveryCharge.applyNumberFormat(0)
//                binding.textViewUserPaymentFinalPayment.applyNumberFormat(totalPrice)
//                binding.textViewUserPaymentTotalPayment.applyNumberFormat(totalPrice)
//            } else {
//                totalPrice += 3000
//                binding.buttonUserCartOrder.text = totalPrice.convertThreeDigitComma()
//                binding.textViewUserPaymentDeliveryCharge.applyNumberFormat(3000)
//                binding.textViewUserPaymentFinalPayment.applyNumberFormat(totalPrice)
//                binding.textViewUserPaymentTotalPayment.applyNumberFormat(totalPrice)
//            }
//
//
//        }

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
                        //Toast.makeText(requireContext(), "계좌이체 선택", Toast.LENGTH_SHORT).show()
                        /*       collapseView(binding.textInputLayoutUserPaymentCard)
                               collapseView(binding.textViewUserPaymentCard)
                               collapseView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentStar.visibility = View.GONE
                        }
                    }

                    R.id.buttonUserPaymentPaymentMethodCard -> {
                        // 신용카드 버튼 선택 시 동작
                        //Toast.makeText(requireContext(), "신용카드 선택", Toast.LENGTH_SHORT).show()
                        /*expandView(binding.textInputLayoutUserPaymentCard)
                        expandView(binding.textViewUserPaymentCard)
                        expandView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.VISIBLE
                            textViewUserPaymentCard.visibility = View.VISIBLE
                            textViewUserPaymentStar.visibility = View.VISIBLE
                        }

                    }

                    R.id.buttonUserPaymentPaymentMethodKakaoPay -> {
                        // 카카오페이 버튼 선택 시 동작
                        //Toast.makeText(requireContext(), "카카오페이 선택", Toast.LENGTH_SHORT).show()
                        /*  collapseView(binding.textInputLayoutUserPaymentCard)
                          collapseView(binding.textViewUserPaymentCard)
                          collapseView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentStar.visibility = View.GONE
                        }
                    }

                    R.id.buttonUserPaymentPaymentMethodNaverPay -> {
                        // 네이버페이 버튼 선택 시 동작
                        //Toast.makeText(requireContext(), "네이버페이 선택", Toast.LENGTH_SHORT).show()
                        /*      collapseView(binding.textInputLayoutUserPaymentCard)
                              collapseView(binding.textViewUserPaymentCard)
                              collapseView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentStar.visibility = View.GONE
                        }
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
                UserPaymentScreenFragmentDirections.actionUserPaymentScreenToUserCartChoiceDeliverAddress()
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
            Log.d("test100", "args == null ")
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

    // 배송 문서 생성
    fun addDelivery(orderId: String, deliverAddressDocId: String) {
        binding.apply {
            // 배송 방식 받아주고
            var deliverOption = DeliveryOption.DOOR_DELIVERY

            when (radioGroupUserPaymentDeliveryMethod.checkedRadioButtonId) {
                R.id.buttonUserPaymentDeliveryMethodBox -> {
                    deliverOption = DeliveryOption.PARCEL_LOCKER
                }

                R.id.buttonUserPaymentDeliveryMethodOffice -> {
                    deliverOption = DeliveryOption.SECURITY_OFFICE
                }
            }

            // 기타 요청사항 받아주고
            val etc = textInputLayoutUserPaymentRequest.editText?.text
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
                    if (binding.checkboxUserPaymentUseSavingAll.isChecked) userReward.toString() else ""
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
                    val userReward = userModel?.customerUserReward ?: -1 // 보유 리워드 (없으면 0)
                    val inputText = s.toString()

                    // 입력값이 숫자인지 확인 후 비교
                    val inputValue = inputText.toIntOrNull() ?: 0

                    if (inputValue > userReward) {
                        // 보유 리워드보다 크면 자동으로 보유 리워드 값으로 설정
                        textInputLayoutUserPaymentSaving.editText?.setText(userReward.toString())
                    }


                    if (inputText.isNotEmpty()) {
                        // 전체 체크 상태에서 값을 변경할때 발생하는 리스너
                        if (inputText.toInt() != userReward) {
                            checkboxUserPaymentUseSavingAll.isChecked = false
                        }

                        // 값을 변경할때 값과 보유 리워드와 같으면 발생하는 리스너
                        if (inputText.toInt() == userReward) {
                            checkboxUserPaymentUseSavingAll.isChecked = true
                        }

                        // 적립금 사용금 표시 메서드 호출
                        settingViewReward()
                        // 총 결제 금액 표시
                        settingViewTotalCost()
                        // 총 결제 금액 ui 변경
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
    fun settingViewDeliverCost() {
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
                binding.textViewUserPaymentDeliveryCharge.text = "3000원"
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
                    "보유 적립금 : ${userModel?.customerUserReward ?: -1}"
            }
        }
    }


    // 적립금 사용금 표시
    fun settingViewReward() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(0)
            binding.apply {
                textViewUserPaymentTotalSavingInfo.text =
                    "${textInputLayoutUserPaymentSaving.editText?.text.toString()}원"
            }
        }
    }

    // 상품 가격 표시
    fun settingViewProductsPrice() {
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
                textViewProductTotalPrice.text = "${sumPrice}원"
            }
        }
    }

    // 총 결제 금액 표시
    fun settingViewTotalCost() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(150)
            binding.apply {
                // 상품 가격
                val productPrice =
                    textViewProductTotalPrice.text.toString().replace("원", "").trim().toIntOrNull()
                        ?: 0

                // 적립금
                val usedReward =
                    textViewUserPaymentTotalSavingInfo.text.toString().replace("원", "").trim()
                        .toIntOrNull() ?: 0

                // 배송비
                val shipmentCost =
                    textViewUserPaymentDeliveryCharge.text.toString().replace("원", "").trim()
                        .toIntOrNull() ?: 0

                val totalCost = productPrice + shipmentCost - usedReward
                textViewUserPaymentTotalPayment.text = "${totalCost}원"
            }
        }
    }

    // 주문 완료 버튼
    fun onClickButtonButtonUserCartOrder() {
        CoroutineScope(Dispatchers.Main).launch {


            val work1= async(Dispatchers.IO){
                val deliverModel = DeliveryModel()
            }


            val work2 = async(Dispatchers.IO){
                // order 생성

                val orderModel = OrderModel()

                orderModel.





                val orderDocId = OrderService.addMyOrder()

            }
            // orderProduct 생성

        }

    }

    fun addOrderProduct() {
        val orderProductModel = OrderProductModel()

    }


}
