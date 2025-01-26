package com.example.frume.fragment.user_fragment.user_cart

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.databinding.FragmentBottomSheetShowCartOptionBinding
import com.example.frume.home.HomeActivity
import com.example.frume.model.CartProductModel
import com.example.frume.service.CartProductService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class BottomSheetShowCartOptionFragment : BottomSheetDialogFragment() {
    private var cartProductModel = CartProductModel()
    lateinit var homeActivity: HomeActivity
    private val args: BottomSheetShowCartOptionFragmentArgs by navArgs()
    lateinit var binding: FragmentBottomSheetShowCartOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_sheet_show_cart_option,
            container,
            false
        )
        gettingCartProduct()
        settingView()
        settingDateDialog()
        onClickAdd()
        onClickMinus()
        // 변경완료 리스너 메서드 실행
        changeCartProductOption()


        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    // args 값으로 cartProduct 가져오는 메서드
    private fun gettingCartProduct() {
        Log.d("test100","gettingProduct")
        CoroutineScope(Dispatchers.Main).launch {

            val work1 = async(Dispatchers.IO) {
                CartProductService.gettingMyCartProductItem(args.cartDocId, args.cartProductDocId)
            }
            cartProductModel = work1.await()
        }
    }

    @SuppressLint("SetTextI18n")
    // 가져온 cartProduct를 기준으로 UI 그린다.
    private fun settingView() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withTimeout(2000) {  // 2000ms = 2초
                    // 2초 동안 cartProductDocId가 비어 있는지 확인
                    while (cartProductModel.cartProductDocId == "") {
                        delay(500)  // 0.5초마다 확인
                    }
                }

                // cartProductDocId가 비어 있지 않으면 정보를 가져옴
                val work1 = async(Dispatchers.IO) {
                    CartProductService.gettingMyCartProductItem(
                        args.cartDocId,
                        args.cartProductDocId
                    )
                }
                cartProductModel = work1.await()
                Log.d("test100","1111${cartProductModel.cartItemProductQuantity}")

                binding.apply {
                    Log.d("test100","22222${cartProductModel.cartItemProductQuantity}")
                    var productQuantity = cartProductModel.cartItemProductQuantity
                    Log.d("test100","333333${productQuantity}")
                    var productSumPrice = cartProductModel.cartProductPrice * productQuantity
                    editTextBottomSheetShowCartOptionProductCount.setText(productQuantity.toString())
                    textViewBottomSheetShowCartOptionPrice.text = productSumPrice.toString()
                    textViewBottomSheetShowCartOptionDate.text =
                        cartProductModel.cartItemDeliveryDueDate.toString()
                    textViewBottomSheetShowCartOptionDate.text =
                        convertToDate(cartProductModel.cartItemDeliveryDueDate)
                }
            } catch (e: TimeoutCancellationException) {
                // 2초가 초과되면 정보가 없다고 처리
                // 정보가 없다는 메시지를 표시하는 등의 처리를 진행
                Log.d("test100", "정보를 가져오는 데 실패했습니다. 2초 내에 데이터를 찾을 수 없음.")
                // 예를 들어 UI에 "정보가 없습니다" 표시
                binding.textViewBottomSheetShowCartOptionPrice.text = "정보가 없습니다"
            }
        }
    }

    // 날짜 선택을 위한 클릭 리스너 설정
    private fun settingDateDialog() {
        binding.viewBottomSheetShowCartOptionDate.setOnClickListener {
            showDatePickerDialog()  // 날짜 선택 다이얼로그 호출
        }
    }


    // 날짜 선택 다이얼로그를 띄우고, 선택한 날짜를 TextView에 업데이트하는 메서드
    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog() {
        // 현재 날짜를 가져와서 초기 날짜로 설정
        val calendar = Calendar.getInstance()

        // 내일 날짜로 설정
        calendar.add(Calendar.DAY_OF_MONTH, 1)  // 하루를 더함

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 한국어 설정
        val locale = Locale("ko", "KR")
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // DatePickerDialog 생성
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerStyle,  // 커스텀 테마 적용
            { _, selectedYear, selectedMonth, selectedDay ->
                // 선택한 날짜를 텍스트로 변환 (예: 2025년 01월 14일)
                val selectedDate = String.format(
                    "%d년 %02d월 %02d일",
                    selectedYear,
                    selectedMonth + 1,  // 월은 0부터 시작하므로 +1 해줘야 함
                    selectedDay
                )

                // 선택한 날짜를 TextView에 표시
                binding.textViewBottomSheetShowCartOptionDate.text = selectedDate
            },
            year, month, day
        )

        // 오늘 이후로만 선택 가능하게 설정 (내일부터)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        // 다이얼로그 확인 취소 버튼 색깔 바꾸기
        val textColor = ContextCompat.getColor(requireActivity(), R.color.black)

        // 다이얼로그 띄우기
        datePickerDialog.show()

        // 버튼 색깔 설정
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setTextColor(textColor)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setTextColor(textColor)
    }

    // timeStamp -> String 변환
    private fun convertToDate(timeStamp: Timestamp): String {
        // Firestore Timestamp를 Date 객체로 변환
        val date = timeStamp.toDate()

        // 원하는 형식으로 날짜 포맷
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

        return dateFormat.format(date)
    }

    // 2025-01-29 형식을 TimeStamp 객체로 변환하기
    private fun convertToTimestamp(dueDate: String): Timestamp {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        dateFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        return try {
            val parsedDate = dateFormatter.parse(dueDate)
            if (parsedDate != null) Timestamp(parsedDate) else Timestamp.now()
        } catch (e: Exception) {
            e.printStackTrace()
            Timestamp.now()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickAdd() {
        binding.apply {
            imageViewBottomSheetShowCartOptionAdd.setOnClickListener {
                // editText의 현재 텍스트 값에서 +1 한 값을 다시 설정
                val cartProductQuantity = editTextBottomSheetShowCartOptionProductCount.text.toString().toIntOrNull() ?: 0
                editTextBottomSheetShowCartOptionProductCount.setText((cartProductQuantity + 1).toString())
                calculation()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickMinus() {
        binding.apply {
            imageViewBottomSheetShowCartOptionRemove.setOnClickListener {
                // editText의 현재 텍스트 값에서 -1 한 값을 다시 설정
                val cartProductQuantity = editTextBottomSheetShowCartOptionProductCount.text.toString().toIntOrNull() ?: 0
                // 음수로 내려가지 않도록 방지
                if (cartProductQuantity > 0) {
                    editTextBottomSheetShowCartOptionProductCount.setText((cartProductQuantity - 1).toString())
                }
                calculation()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculation() {
        binding.apply {
            val totalPrice = editTextBottomSheetShowCartOptionProductCount.text.toString().toInt() * cartProductModel.cartProductUnitPrice
            textViewBottomSheetShowCartOptionPrice.text = totalPrice.toString() // Int를 String으로 변환하여 설정
        }
    }

    // 변경 완료 텍스트 누를때 리스너
    private fun changeCartProductOption() {
        binding.apply {
            textViewBottomSheetShowCartOptionChangeProductOption.setOnClickListener{
                // 수량 설정
                cartProductModel.cartItemProductQuantity = editTextBottomSheetShowCartOptionProductCount.text.toString().toInt()
                // 변경된 날짜 가져오기
                val changedDate = textViewBottomSheetShowCartOptionDate.text.toString()
                // 변경된 가격
                val changedCartProductPrice = editTextBottomSheetShowCartOptionProductCount.text.toString().toInt() * cartProductModel.cartProductPrice
                // 예정일 설정
                cartProductModel.cartItemDeliveryDueDate = convertToTimestamp(changedDate)
                cartProductModel.cartProductPrice = changedCartProductPrice

                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        CartProductService.changeCartProductOption(args.cartDocId,args.cartProductDocId,cartProductModel)
                    }
                    work1.join()
                    dismiss()
                }
            }
        }
    }


}

