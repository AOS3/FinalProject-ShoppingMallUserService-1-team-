package com.example.frume.fragment.user_fragment.user_cart

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data.Storage
import com.example.frume.data_ye.DummyData
import com.example.frume.data_ye.TempCartProduct
import com.example.frume.databinding.FragmentUserCart1Binding
import com.example.frume.databinding.ItemUsercartListBinding
import com.example.frume.fragment.home_fragment.user_home.HomeProductAdapter
import com.example.frume.home.HomeActivity
import com.example.frume.service.UserService
import com.example.frume.util.applyNumberFormat
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Indicator
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class UserCartFragment1 : Fragment(), CartClickListener {
    private var _binding: FragmentUserCart1Binding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserCartAdapter
    private lateinit var cartList: MutableList<TempCartProduct>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_cart1, container, false)
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


    private fun setLayout() {
        // RecyclerView 설정 메서드 호출
        settingRecyclerViewUserCartProduct()
        getUserData()
        // 버튼 클릭 리스너 설정
        onClickCartOrderProduct()
        onClickCartDeliverySpotChange()
        settingDateDialog()
        onClickCartRemoveBtn()
        onClickAllCheckBox()
        getTotalPrice()
    }

    // sehoon 총 가격을 가져오는 메서드
    private fun getTotalPrice() {
        binding.textViewUserCartDialogPrice.applyNumberFormat(adapter.getTotalPrice())
    }

    // 유저 정보를 가져오는 메서드
    private fun getUserData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userDocId = activity as HomeActivity
            val userList = UserService.getUserInfo(userDocId.loginUserDocumentId)
            withContext(Dispatchers.Main) {
                with(binding) {
                    userList.forEach {
                        textViewUserCartUserName.text = it.customerUserName
                        textViewUserCartUserPhoneNumber.text = it.customerUserPhoneNumber
                        val address1 = it.customerUserAddress["BasicAddress"]
                        val address2 = it.customerUserAddress["DetailedAddress"]
                        val address3 = it.customerUserAddress["PostNumber"]
                        textViewUserCartUserAddress.text = "$address1" + "$address2" + "$address3"
                    }
                }

            }
        }
    }

    // 날짜 선택을 위한 클릭 리스너 설정
    private fun settingDateDialog() {
        binding.viewUserCartDialogDeliveryDate.setOnClickListener {
            showDatePickerDialog()  // 날짜 선택 다이얼로그 호출
        }
    }

    // 배송지 변경 버튼 클릭 시, UserAddressManageFragment로 이동하는 메서드
    private fun onClickCartDeliverySpotChange() {
        binding.buttonUserCartDialogModifyAddress.setOnClickListener {
            // 네비게이션을 통해 UserCartChoiceDeliveryAddressFragment로 이동
            // 이동화면 변경 hj
            val action = UserCartFragmentDirections.actionNavigationCartToUserCartChoiceDeliverAddress()
            findNavController().navigate(action)
        }
    }

    // 구매하기 버튼 클릭 시, UserPaymentScreenFragment로 이동하는 메서드
    private fun onClickCartOrderProduct() {
        binding.buttonUserCartOrder.setOnClickListener {
            // 네비게이션을 통해 UserPaymentScreenFragment로 이동
            val userDocId = activity as HomeActivity
            // sehoon 장바구니 -> 저장
            val action = UserCartFragmentDirections.actionNavigationCartToUserPaymentScreen(userDocId.loginUserDocumentId)
            findNavController().navigate(action)
        }
    }

    // RecyclerView 설정 메서드
    private fun settingRecyclerViewUserCartProduct() {
        // 사용자의 장바구니를 가져옴
        cartList = DummyData.CartItemList.toMutableList()
        adapter = UserCartAdapter(cartList.toMutableList(), this)
        binding.recyclerViewUserCart1.adapter = adapter
    }

    // 개별 삭제 메서드
    private fun onClickCartRemoveBtn() {
        binding.textViewButtonUserCartDelete.setOnClickListener {
            val emptyCheck = adapter.deleteSelectedItems()
            getTotalPrice()
            // 리싸이클러뷰가 비어있으면 전체 선택 체크박스 해제
            if (emptyCheck) {
                binding.checkboxUserCartSelectAll.isChecked = false
            }
        }
    }

    // 전체 선택 체크박스 메소드
    private fun onClickAllCheckBox() {
        with(binding) {
            checkboxUserCartSelectAll.setOnClickListener {
                if (checkboxUserCartSelectAll.isChecked) {
                    adapter.onClickAllCheckBox(true)
                } else {
                    adapter.onClickAllCheckBox(false)
                }
            }
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
                binding.textViewUserCartDialogDeliveryDate.text = selectedDate
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

    override fun onClickAdd(pos: Int, cartProduct: TempCartProduct) {
        if (cartProduct.quantity == 10) {
            Toast.makeText(requireContext(), "최대 구매 개수는 10개 입니다.", Toast.LENGTH_SHORT).show()
        } else {
            adapter.onClickAdd(pos)
            getTotalPrice()
        }
    }

    override fun onClickMinus(pos: Int, cartProduct: TempCartProduct) {
        if (cartProduct.quantity == 1) {
            Toast.makeText(requireContext(), "최소 구매 개수는 1개 입니다.", Toast.LENGTH_SHORT).show()
        } else {
            adapter.onClickMinus(pos)
            getTotalPrice()
        }
    }


    override fun onClickCheckBox() {
        binding.checkboxUserCartSelectAll.setOnClickListener {
            val isChecked = binding.checkboxUserCartSelectAll.isChecked
            adapter.onClickAllCheckBox(isChecked)
        }
    }

    override fun onClickItemCheckBox(pos: Int, cartProduct: TempCartProduct) {
        cartProduct.productCheck = !cartProduct.productCheck
        when {
            adapter.isCheckBox() -> {
                binding.checkboxUserCartSelectAll.isChecked = true
            }
            adapter.isCheckBoxAny() -> {
                binding.checkboxUserCartSelectAll.checkedState = MaterialCheckBox.STATE_INDETERMINATE
            }
            else -> {
                binding.checkboxUserCartSelectAll.isChecked = false
            }
        }
    }
}

