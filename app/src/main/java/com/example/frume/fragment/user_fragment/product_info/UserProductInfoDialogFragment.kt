package com.example.frume.fragment.user_fragment.product_info

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoDialogBinding
import com.example.frume.home.HomeActivity
import com.example.frume.model.CartProductModel
import com.example.frume.service.CartProductService
import com.example.frume.service.ProductService
import com.example.frume.service.CartService
import com.example.frume.util.CartProductIsPurchasesBoolType
import com.example.frume.util.CartProductState
import com.example.frume.util.DeliveryCycleDays
import com.example.frume.util.DeliveryCycleWeeks
import com.example.frume.util.DeliverySubscribeState
import com.example.frume.util.applyNumberFormat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserProductInfoDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentUserProductInfoDialogBinding? = null
    private val binding get() = _binding!!
    private var productPrice = 0
    private var _productPrice = 0
    private var productCount = 1
    private val args: UserProductInfoDialogFragmentArgs by navArgs()

    // 액티비티 할당 변수
    lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 액티비티 할당
        homeActivity = activity as HomeActivity
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_product_info_dialog,
            container,
            false
        )
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


    private fun settingBottomSheet() {
        // BottomSheetBehavior 가져오기
        val dialog = dialog as? com.google.android.material.bottomsheet.BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)

            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels

            behavior.peekHeight = (screenHeight * 0.8).toInt()
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setLayout() {
        getProductData()
        onClickPaymentBtn()
        onClickCalendarDialog()
        settingBottomSheet()
        onClickCountBtn()
        watcherProductCount()
        // 장바구니 담기
        onClickButtonCategory()
    }

    private fun getProductData() {
        val productDocId = args.productDocId
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val productList = productDocId?.let { ProductService.getProductInfo(it) }
                withContext(Dispatchers.Main) {
                    // UI
                    productList?.forEach { item ->
                        productPrice = item.productPrice
                        with(binding) {
                            textViewProductName.text = item.productName
                            textViewProductDescription.text = item.productDescription
                            textViewUserProductInfoDialogPrice.applyNumberFormat(item.productPrice)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "no data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // sehoon 배송 예정일 클릭 메소드
    private fun onClickCalendarDialog() {
        binding.viewUserProductInfoDialogDeliveryDate.setOnClickListener {
            showMaterialDatePicker { selectedDate ->
                if (selectedDate.isEmpty()) {
                    binding.textViewUserProductInfoDialogDeliveryDate.text = "배송 예정일 선택"
                }
                binding.textViewUserProductInfoDialogDeliveryDate.text = selectedDate
            }
        }
    }


    private fun showMaterialDatePicker(onDateSelected: (String) -> Unit) {
        // 내일부터 선택 가능하도록 제약 조건 추가
        val today = Calendar.getInstance()
        today.add(Calendar.DAY_OF_MONTH, 0) // 내일로 설정
        val tomorrowInMillis = today.timeInMillis

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(object : CalendarConstraints.DateValidator {
                override fun isValid(date: Long): Boolean {
                    return date >= tomorrowInMillis // 내일부터 선택 가능
                }

                override fun writeToParcel(dest: Parcel, flags: Int) {}
                override fun describeContents(): Int = 0
            })

        // MaterialDatePicker 빌더 설정
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("배송 예정일 선택")
            .setTheme(R.style.ThemeOverlay_Questionnaire_DatePicker)
            .setCalendarConstraints(constraintsBuilder.build())
            .setSelection(tomorrowInMillis) // 기본 선택 날짜: 내일
            .build()

        // 날짜 선택 이벤트 처리
        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = dateFormat.format(calendar.time)

            Toast.makeText(requireContext(), "선택된 날짜: $selectedDate", Toast.LENGTH_SHORT).show()
            onDateSelected(selectedDate) // 콜백 호출
        }

        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
    }

    private fun watcherProductCount() {
        binding.editTextProductInfoDialogCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 값 변경 전 처리 (필요 시)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 실시간 입력 감지 (필요 시)
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                if (input.isNotEmpty()) {
                    productCount = input.toIntOrNull() ?: 0

                    when {
                        productCount > 10 -> {
                            // 최대 개수 초과 처리
                            Toast.makeText(
                                requireContext(),
                                "최대 구매 개수는 10개 입니다",
                                Toast.LENGTH_SHORT
                            ).show()
                            productCount = 10
                            binding.editTextProductInfoDialogCount.setText("$productCount")
                            binding.editTextProductInfoDialogCount.setSelection(productCount.toString().length)
                        }

                        productCount < 0 -> {
                            // 음수 값 처리
                            Toast.makeText(
                                requireContext(),
                                "구매 개수는 0개 이상이어야 합니다",
                                Toast.LENGTH_SHORT
                            ).show()
                            productCount = 0
                            binding.editTextProductInfoDialogCount.setText("$productCount")
                            binding.editTextProductInfoDialogCount.setSelection(productCount.toString().length)
                        }
                    }

                    // 총 가격 업데이트
                    _productPrice = productPrice * productCount
                    binding.textViewUserProductInfoDialogPrice.applyNumberFormat(_productPrice)
                } else {
                    // 입력값이 비어 있을 경우
                    _productPrice = 0
                    binding.textViewUserProductInfoDialogPrice.applyNumberFormat(_productPrice)
                }
            }
        })
    }

    private fun onClickCountBtn() {
        // 증가 버튼
        binding.imageViewProductInfoDialogAdd.setOnClickListener {
            productCount = binding.editTextProductInfoDialogCount.text.toString().toIntOrNull() ?: 0
            if (productCount >= 10) {
                Toast.makeText(requireContext(), "최대 구매 개수는 10개 입니다", Toast.LENGTH_SHORT).show()
            } else {
                productCount++
                _productPrice = productPrice * productCount
                binding.editTextProductInfoDialogCount.setText("$productCount")
                binding.textViewUserProductInfoDialogPrice.applyNumberFormat(_productPrice)
            }
        }

        // 감소 버튼
        binding.imageViewProductInfoDialogRemove.setOnClickListener {
            productCount = binding.editTextProductInfoDialogCount.text.toString().toIntOrNull() ?: 0
            if (productCount <= 0) {
                Toast.makeText(requireContext(), "0개 입니다", Toast.LENGTH_SHORT).show()
            } else {
                productCount--
                _productPrice = productPrice * productCount
                binding.editTextProductInfoDialogCount.setText("$productCount")
                binding.textViewUserProductInfoDialogPrice.applyNumberFormat(_productPrice)
            }
        }
    }

    private fun onClickPaymentBtn() {
        binding.buttonUserProductInfoDialogBuy.setOnClickListener {
            val userDocId = activity as HomeActivity
            if (binding.textViewUserProductInfoDialogDeliveryDate.text == "배송 예정일 선택") {
                Toast.makeText(requireContext(), "배송일을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
                if (productCount == 1) {
                    val action = UserProductInfoDialogFragmentDirections.actionUserProductInfoDialogToUserPaymentScreen(userDocId.loginUserDocumentId)
                    findNavController().navigate(action)
                } else {
                    val action = UserProductInfoDialogFragmentDirections.actionUserProductInfoDialogToUserPaymentScreen(userDocId.loginUserDocumentId)
                    findNavController().navigate(action)
                }
            }
        }
    }


     fun addToCategory() {
        // 담는 경우라면
        CoroutineScope(Dispatchers.Main).launch {

            // 상품 수량
            val cartProductCount = binding.editTextProductInfoDialogCount.text.toString()
            // 배송 예정일
            val dueDate = binding.textViewUserProductInfoDialogDeliveryDate.text.toString()

            if (binding.textViewUserProductInfoDialogDeliveryDate.text == "배송 예정일 선택") {
                Toast.makeText(requireContext(), "배송일을 선택해주세요", Toast.LENGTH_SHORT).show()
                return@launch
            }

            Log.d("test100", "dueDate : $dueDate")
            // 내 장바구니 품목에 있는지 여부 값
            var isInMyCart = false
            // 유저 DOC ID
            val userDocId = homeActivity.loginUserDocumentId

            // 내 장바구니를 가져온다.
            val work1 = async(Dispatchers.IO) {
                CartService.gettingMyCart(userDocId)
            }
            // 내 장바구니
            val myCartModel = work1.await()


            // 내 장바구니에 아이템들 가져오기
            val work3 = async(Dispatchers.IO) {
                CartProductService.gettingMyCartProductItems(myCartModel.cartDocId)
            }
            // 내 장바구니 아이템 목록
            val selectedCartProductModelItems = work3.await()

            // 장바구니 아이템을 탐색한다.
            selectedCartProductModelItems.forEach {
                // 같은게 있다면 상태값 바꾸고 for문 탈출한다
                if (it.cartItemProductDocId == args.productDocId) {
                    isInMyCart = true
                    return@forEach
                }
            }
            if (isInMyCart) {
                // 이미 존재하는 상품입니다. Dialog
                showConfirmationDialog("이미 존재하는 상품입니다.","","확인","",fun(){},fun(){})
                return@launch
            }

            // cartProductModel 생성
            val cartProductModel = convertToCartProduct(cartProductCount,dueDate,"${args.productDocId}",myCartModel.cartDocId)

            // 내 cart 에 cartProduct 담기
            CartProductService.addMyCartProduct(myCartModel.cartDocId, cartProductModel)

            // 담았다면 내 장바구니로 이동할건지 체크
            showConfirmationDialog("장바구니로 이동하시겠습니까?","장바구니에 상품을 담았습니다.","네","아니오",fun(){

                val action = UserProductInfoDialogFragmentDirections.actionUserProductInfoDialogToNavigationCart()
                findNavController().navigate(action)
            })

        }
    }

    // 장바구니 담기 메서드
    private fun onClickButtonCategory() {
        binding.buttonUserProductInfoDialogCart.setOnClickListener {

            // 담을지 말지 다이얼로그 띄운다.
            showConfirmationDialog("장바구니에 담으시겠습니까?","","네","아니오",fun(){
                addToCategory()
            },)
        }
    }
    // cartProduct로 변환하기
    fun convertToCartProduct(
        productCount: String,
        dueDate: String,
        productDocId: String,
        cartDocId: String
    ): CartProductModel {
        val cartProductModel = CartProductModel()

        // dueDate를 Timestamp로 변환하여 할당
        cartProductModel.cartItemDeliveryDueDate = convertToTimestamp(dueDate)

        // 필요한 변환 작업 수행
        cartProductModel.cartItemProductQuantity = productCount.toIntOrNull() ?: 0
        cartProductModel.cartItemProductDocId = productDocId
        cartProductModel.cartDocId = cartDocId
        cartProductModel.customerDocId = homeActivity.loginUserDocumentId
        // 비구독
        cartProductModel.cartItemIsSubscribed = DeliverySubscribeState.DELIVERY_STATE_NOT_SUBSCRIBE
        // 등록시간
        cartProductModel.cartItemDeliveryTimeStamp = Timestamp.now()
        cartProductModel.cartItemDeliveryCycleWeek = DeliveryCycleWeeks.DELIVERY_CYCLE_WEEKS_NONE
        cartProductModel.cartItemDeliveryCycleDay = DeliveryCycleDays.DELIVERY_CYCLE_DAYS_NONE
        cartProductModel.cartItemIsPurchases = CartProductIsPurchasesBoolType.CART_PRODUCT_IS_PURCHASES_TRUE
        cartProductModel.cartProductState = CartProductState.CART_PRODUCT_STATE_NORMAL

        return cartProductModel
    }

    // 2025-01-29 형식을 TimeStamp 객체로 변환하기
    fun convertToTimestamp(dueDate: String): Timestamp {
        val dateFormatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREAN)
        dateFormatter.timeZone = java.util.TimeZone.getTimeZone("Asia/Seoul")

        return try {
            val parsedDate = dateFormatter.parse(dueDate)
            if (parsedDate != null) Timestamp(parsedDate) else Timestamp.now()
        } catch (e: Exception) {
            e.printStackTrace()
            Timestamp.now()
        }
    }

    // 다이얼로그를 통해 메시지를 보여주는 함수
    fun showConfirmationDialog(
        title: String,
        message: String,
        positiveTitle: String = "네",
        negativeTitle: String = "아니요",
        onPositive: () -> Unit,
        onNegative: () -> Unit = {}
    ) {
        val builder = MaterialAlertDialogBuilder(homeActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveTitle) { dialogInterface: DialogInterface, i: Int ->
            onPositive()
        }
        builder.setNegativeButton(negativeTitle) { dialogInterface: DialogInterface, i: Int ->
            onNegative()
        }
        builder.show()
    }
}


