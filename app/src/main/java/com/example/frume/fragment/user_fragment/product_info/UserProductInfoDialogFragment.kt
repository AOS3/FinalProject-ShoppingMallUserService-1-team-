package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.os.Parcel
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoDialogBinding
import com.example.frume.util.applyNumberFormat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_dialog, container, false)

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
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)

            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels

            behavior.peekHeight = (screenHeight * 0.8).toInt()
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setLayout() {
        onClickPaymentBtn()
        val productName = args.productMethod
        productPrice = args.productPrice
        binding.tvProductName.text = productName
        binding.textViewUserProductInfoDialogPrice.applyNumberFormat(productPrice)
        binding.imageViewUserProductDialogThumbNail.setImageResource(args.productImg)
        onClickCalendarDialog()
        settingBottomSheet()
        onClickCountBtn()
        watcherProductCount()
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
                            Toast.makeText(requireContext(), "최대 구매 개수는 10개 입니다", Toast.LENGTH_SHORT).show()
                            productCount = 10
                            binding.editTextProductInfoDialogCount.setText("$productCount")
                            binding.editTextProductInfoDialogCount.setSelection(productCount.toString().length)
                        }

                        productCount < 0 -> {
                            // 음수 값 처리
                            Toast.makeText(requireContext(), "구매 개수는 0개 이상이어야 합니다", Toast.LENGTH_SHORT).show()
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
            if (binding.textViewUserProductInfoDialogDeliveryDate.text == "배송 예정일 선택") {
                Toast.makeText(requireContext(), "배송일을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val action = UserProductInfoDialogFragmentDirections.actionUserProductInfoDialogToUserPaymentScreen()
                findNavController().navigate(action)
            }
        }
    }

}

