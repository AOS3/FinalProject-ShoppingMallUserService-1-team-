package com.example.frume.fragment.user_fragment.user_cart

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data_ye.DummyData
import com.example.frume.data_ye.TempCartProduct
import com.example.frume.databinding.FragmentUserCart1Binding
import com.example.frume.databinding.ItemUsercartListBinding
import java.util.Calendar
import java.util.Locale

class UserCartFragment1 : Fragment() {

    private var _binding: FragmentUserCart1Binding? = null
    private val binding get() = _binding!!

    // 전체 선택 상태를 관리하는 변수
    private var isAllSelected = false

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

        // RecyclerView 설정 메서드 호출
        settingRecyclerViewUserCartProduct()

        // 버튼 클릭 리스너 설정
        onClickCartOrderProduct()
        onClickCartDeliverySpotChange()

        // 전체 선택 체크박스 클릭 리스너 설정
        binding.checkboxUserCartSelectAll.setOnCheckedChangeListener { _, isChecked ->
            // 체크박스 상태를 반영하여 전체 선택 상태 갱신
            isAllSelected = isChecked
            updateItemSelection() // 아이템 선택 상태 업데이트
        }

        // 날짜 선택을 위한 클릭 리스너 설정
        binding.viewUserCartDialogDeliveryDate.setOnClickListener {
            showDatePickerDialog()  // 날짜 선택 다이얼로그 호출
        }
    }

    // 배송지 변경 버튼 클릭 시, UserAddressManageFragment로 이동하는 메서드
    private fun onClickCartDeliverySpotChange() {
        binding.buttonUserCartDialogModifyAddress.setOnClickListener {
            // 네비게이션을 통해 UserAddressManageFragment로 이동
            val action = UserCartFragmentDirections.actionNavigationCartToUserAddressManage()
            findNavController().navigate(action)
        }
    }

    // 구매하기 버튼 클릭 시, UserPaymentScreenFragment로 이동하는 메서드
    private fun onClickCartOrderProduct() {
        binding.buttonUserCartOrder.setOnClickListener {
            // 네비게이션을 통해 UserPaymentScreenFragment로 이동
            val action = UserCartFragmentDirections.actionNavigationCartToUserPaymentScreen()
            findNavController().navigate(action)
        }
    }

    // RecyclerView 설정 메서드
    private fun settingRecyclerViewUserCartProduct() {
        binding.recyclerViewUserCart1.apply {
            // LinearLayoutManager를 사용하여 RecyclerView가 세로로 스크롤 되도록 설정
            layoutManager = LinearLayoutManager(context)
            // RecyclerView에 어댑터 설정
            // DummyData.CartItemList에 포함된 데이터를 어댑터로 전달하여 화면에 표시
            adapter = RecyclerViewUserCartProductAdapter(DummyData.CartItemList)
        }
    }

    // 전체 선택 상태에 따라 RecyclerView 아이템들의 선택 상태를 업데이트
    private fun updateItemSelection() {
        val adapter = binding.recyclerViewUserCart1.adapter as RecyclerViewUserCartProductAdapter
        // 모든 아이템의 선택 상태를 갱신
        adapter.updateAllItemsSelected(isAllSelected)
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

    // RecyclerView의 어댑터
    private inner class RecyclerViewUserCartProductAdapter(private val cartItems: List<TempCartProduct>) :
        RecyclerView.Adapter<RecyclerViewUserCartProductAdapter.ViewHolderUserCartProduct>() {

        // 각 아이템의 선택 상태를 추적하는 리스트 (초기값은 모두 선택되지 않음)
        private val itemSelection = MutableList(cartItems.size) { false }

        // 각 아이템의 수량을 추적하는 리스트 (초기값은 모두 0)
        private val itemQuantity = MutableList(cartItems.size) { 1 }

        // ViewHolder (각 아이템을 바인딩할 뷰 홀더)
        private inner class ViewHolderUserCartProduct(val binding: ItemUsercartListBinding) :
            RecyclerView.ViewHolder(binding.root) {

            init {
                // 각 아이템의 체크박스를 클릭하면 해당 아이템의 선택 상태를 업데이트
                binding.checkboxRecyclerViewSelect.setOnCheckedChangeListener { _, isChecked ->
                    itemSelection[adapterPosition] = isChecked
                    updateSelectAllCheckbox()
                }

                // 수량 감소 버튼 클릭 리스너
                binding.imageViewRecyclerViewRemove.setOnClickListener {
                    // 수량 감소 (0보다 작지 않도록 처리)
                    if (itemQuantity[adapterPosition] > 1) {
                        itemQuantity[adapterPosition]--
                        updateQuantityText(adapterPosition)
                    }
                }

                // 수량 증가 버튼 클릭 리스너
                binding.imageViewRecyclerViewAdd.setOnClickListener {
                    // 수량 증가 (99보다 많지 않도록 처리)
                    if (itemQuantity[adapterPosition] < 99) {
                        itemQuantity[adapterPosition]++
                        updateQuantityText(adapterPosition)
                    }
                }
            }
        }

        // 새로운 ViewHolder를 생성하는 메서드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserCartProduct {
            // Item 레이아웃을 인플레이트하여 ViewHolder에 바인딩
            val binding = ItemUsercartListBinding.inflate(layoutInflater, parent, false)
            return ViewHolderUserCartProduct(binding)
        }

        // 아이템의 개수를 반환하는 메서드
        override fun getItemCount(): Int {
            return cartItems.size
        }

        // 아이템에 데이터를 바인딩하는 메서드
        override fun onBindViewHolder(holder: ViewHolderUserCartProduct, position: Int) {
            val item = cartItems[position]

            // 아이템의 데이터와 레이아웃을 연결
            holder.binding.apply {
                textViewRecyclerViewProductName.text = item.productName
                textViewRecyclerViewProductPrice.text = "${item.productPrice}원"
                imageViewRecyclerViewImage.setImageResource(item.imageResId)

                // 해당 아이템의 선택 상태를 체크박스에 반영
                checkboxRecyclerViewSelect.isChecked = itemSelection[position]

                // 수량을 EditText에 반영
                editTextProductCount.setText(itemQuantity[position].toString()) // 수정된 부분
            }
        }

        // 전체 선택 상태에 따라 모든 아이템 선택 여부를 업데이트하는 메서드
        fun updateAllItemsSelected(isSelected: Boolean) {
            itemSelection.fill(isSelected)
            notifyDataSetChanged()
        }

        // 모든 아이템의 선택 상태를 확인하여 전체 선택 체크박스를 업데이트
        private fun updateSelectAllCheckbox() {
            val allSelected = itemSelection.all { it }
            if (allSelected != isAllSelected) {
                isAllSelected = allSelected
                binding.checkboxUserCartSelectAll.isChecked = isAllSelected
            }
        }

        // 수량 변경 후 EditText에 반영하는 메서드
        private fun updateQuantityText(position: Int) {
            // 수량을 EditText에 업데이트
            val itemQuantityText = itemQuantity[position].toString() // 수정된 부분
            val viewHolder = binding.recyclerViewUserCart1.findViewHolderForAdapterPosition(position) as? ViewHolderUserCartProduct
            viewHolder?.binding?.editTextProductCount?.setText(itemQuantityText)
        }
    }

}

