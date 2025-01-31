package com.example.frume.fragment.user_fragment.user_cart

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCartBinding
import com.example.frume.databinding.ItemUsercartListBinding
import com.example.frume.home.HomeActivity
import com.example.frume.model.CartModel
import com.example.frume.model.CartProductModel
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.service.CartProductService
import com.example.frume.service.CartService
import com.example.frume.service.UserDeliveryAddressService
import com.example.frume.util.convertThreeDigitComma
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.Calendar
import java.util.Locale


class UserCartFragment : Fragment() {
    private var _binding: FragmentUserCartBinding? = null
    private val binding get() = _binding!!

    lateinit var homeActivity: HomeActivity

    // 배송지를 담을 변수 처음엔 기본배송지를 담을 예정
    var deliveryAddressSpot: DeliveryAddressModel? = null

    // private val args: UserCartFragmentArgs by nav()
    var cartProductList = mutableListOf<CartProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeActivity = activity as HomeActivity
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_cart, container, false)

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

    override fun onResume() {
        super.onResume()
        // 배송지를 변경하고 화면을 다시 그린 경우 변경된 정보로 다시 ui 세팅한다.
        settingReceiverInfo()
    }

    private fun setLayout() {

        // 배송지 정보를 토대로 레이아웃을 그리는 메서드
        settingReceiverInfo()
        // 배송지 정보를 가져와 deliverSpot에 설정해주는 메서드
        getReceiverData()
        // 카트 품목을 가져와 카트품목을 구한다.
        settingCartProductList()
        // 날짜 선택을 위한 클릭 리스너 메서드 실행
        /*settingDateDialog()*/
        /*// UserPaymentScreenFragment로 이동하는 메서드 실행
        onClickCartOrderProduct()*/
        /*// 배송지 변경 화면으로 이동하는 메서드 실행
        onClickCartDeliverySpotChange()*/
        // RecyclerView 어뎁터 세팅 실행
        settingRecyclerView()

        // RecyclerView 설정 메서드 호출
        /*settingRecyclerViewUserCartProduct()*/
        /*onClickCartRemoveBtn()*/
        /*onClickAllCheckBox()*/
        /*getTotalPrice()*/

    }


    // 배송지를 유저정보에서 가져오지 않고, 배송지 DB에서 기본배송지를 가져오는 것으로 수정 hj 받는사람 이름도 DeliveryAddres Model 에 추가함
    private fun getReceiverData() {
        // 배송지에서 기본 배송지를 가져온다
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                UserDeliveryAddressService.gettingDefaultDeliveryAddress(homeActivity.loginUserDocumentId)
            }
            deliveryAddressSpot = work1.await()
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
                        val receiverName = it.deliveryAddressReceiverName
                        val basicAddress = it.deliveryAddressBasicAddress
                        val detailAddress = it.deliveryAddressDetailAddress
                        val phoneNumber = it.deliveryAddressPhoneNumber

                        // UI에 배송지 정보 입력
                        textViewUserCartUserName.text = receiverName
                        textViewUserCartUserAddress.text = "${basicAddress}  ${detailAddress}"
                        textViewUserCartUserPhoneNumber.text = phoneNumber
                    }
                } catch (e: TimeoutCancellationException) {
                    // 2초 이내에 배송지 정보가 로드되지 않으면 타임아웃 처리
                    // 여기서 타임아웃 후 처리를 할 수 있음 (예: 로딩 실패 메시지 표시)
                    textViewUserCartUserName.text = "배송지 로드 실패"
                    textViewUserCartUserAddress.text = "배송지 정보를 불러올 수 없습니다."
                    textViewUserCartUserPhoneNumber.text = "잠시 후 다시 시도해주세요."
                }

                work1.join()

                // 주문하기 버튼 누를 시 현재 리스트 전체 삭제
                cartProductList.removeAll(cartProductList)

                // sehoon 장바구니 -> 저장
                val action =
                    UserCartFragmentDirections.actionNavigationCartToUserPaymentScreen(null,"Cart")
                findNavController().navigate(action)
            }
        }
    }
    /*// 날짜 선택을 위한 클릭 리스너 설정
    private fun settingDateDialog() {
        binding.viewUserCartDialogDeliveryDate.setOnClickListener {
            showDatePickerDialog()  // 날짜 선택 다이얼로그 호출
        }
    }*/

    /*// 배송지 변경 버튼 클릭 시, UserAddressManageFragment로 이동하는 메서드
    private fun onClickCartDeliverySpotChange() {
        binding.buttonUserCartDialogModifyAddress.setOnClickListener {
            // 네비게이션을 통해 UserCartChoiceDeliveryAddressFragment로 이동
            // 이동화면 변경 hj
            val action = UserCartFragmentDirections.actionNavigationCartToUserCartChoiceDeliverAddress()
            findNavController().navigate(action)
        }
    }*/

    /*// 구매하기 버튼 클릭 시, UserPaymentScreenFragment로 이동하는 메서드
    private fun onClickCartOrderProduct() {
        binding.buttonUserCartOrder.setOnClickListener {
            // 네비게이션을 통해 UserPaymentScreenFragment로 이동
            val userDocId = activity as HomeActivity
            // sehoon 장바구니 -> 저장
            val action = UserCartFragmentDirections.actionNavigationCartToUserPaymentScreen()
            findNavController().navigate(action)
        }
    }*/

    // 내 카트를 가져와, 카트 품목들을 가져온다
    // 품목을 cartProductList에 담는다
    private fun settingCartProductList() {
        var cartModel: CartModel
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                CartService.gettingMyCart(homeActivity.loginUserDocumentId)
            }
            cartModel = work1.await()

            val work2 = async(Dispatchers.IO) {
                CartProductService.gettingMyCartProductItems(cartModel.cartDocId)
            }
            // 장바구니 품목들을 가져온다.
            cartProductList = work2.await()
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

                /*// 선택한 날짜를 TextView에 표시
                binding.textViewUserCartDialogDeliveryDate.text = selectedDate*/
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

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView() {

        binding.apply {
            // CoroutineScope 사용
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // 배송지 정보가 null이 아니거나 값이 설정될 때까지 반복 (2초 제한)
                    withTimeout(2000) {  // 2000ms = 2초
                        while (cartProductList.size == 0) {
                            delay(500)  // 0.5초마다 확인
                        }
                    }

                    // 어뎁터
                    recyclerViewUserCart1.adapter = RecyclerViewCart1Adapter()
                    // LayoutManager
                    //     recyclerViewUserCart1.layoutManager = LinearLayoutManager(homeActivity)
                    // 구분선
//                     val deco = MaterialDividerItemDecoration(homeActivity, MaterialDividerItemDecoration.VERTICAL)
//                     recyclerViewUserCart1.addItemDecoration(deco)

                } catch (e: TimeoutCancellationException) {
                    // 2초 이내에 배송지 정보가 로드되지 않으면 타임아웃 처리
                    // 여기서 타임아웃 후 처리를 할 수 있음 (예: 로딩 실패 메시지 표시)
                    // 어뎁터
                    recyclerViewUserCart1.adapter = RecyclerViewCart1Adapter()

                }
            }
        }
        /*binding.apply {
            // 어뎁터
            recyclerViewUserCart1.adapter = RecyclerViewCart1Adapter()
            // LayoutManager
            recyclerViewUserCart1.layoutManager = LinearLayoutManager(homeActivity)
            // 구분선
            val deco = MaterialDividerItemDecoration(homeActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewUserCart1.addItemDecoration(deco)
        }*/
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewCart1Adapter : RecyclerView.Adapter<RecyclerViewCart1Adapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(val itemCartListBinding: ItemUsercartListBinding) : RecyclerView.ViewHolder(itemCartListBinding.root),
            View.OnClickListener {
            override fun onClick(v: View?) {
                //
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {

            //
            val itemProductListBinding = ItemUsercartListBinding.inflate(layoutInflater, parent, false)

            val viewHolderProductItem = ViewHolderMain(itemProductListBinding)

            // 리스너를 설정해준다.
            itemProductListBinding.root.setOnClickListener(viewHolderProductItem)

            return viewHolderProductItem
        }

        override fun getItemCount(): Int {
            return cartProductList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            // holder.itemCartListBinding.text
            holder.itemCartListBinding.textViewRecyclerViewProductName.text = cartProductList[position].cartProductName
            holder.itemCartListBinding.textViewRecyclerViewProductPrice.text = cartProductList[position].cartProductPrice.convertThreeDigitComma()
            val productQuantity = cartProductList[position].cartItemProductQuantity
            /*holder.itemCartListBinding.editTextProductCount.setText("$productQuantity")*/
            holder.itemCartListBinding.checkboxRecyclerViewSelect.isChecked = cartProductList[position].cartItemIsCheckState.bool

        }
    }


    /*    // sehoon 총 가격을 가져오는 메서드
    private fun getTotalPrice() {
        binding.textViewUserCartDialogPrice.applyNumberFormat(adapter.getTotalPrice())
    }*/


    /*override fun onClickAdd(pos: Int, cartProduct: TempCartProduct) {
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
    }*/


    /*
        // RecyclerView 설정 메서드
        private fun settingRecyclerViewUserCartProduct() {
            // 사용자의 장바구니를 가져옴
            cartList = DummyData.CartItemList.toMutableList()
            adapter = UserCartAdapter(cartList.toMutableList(), this)
            binding.recyclerViewUserCart1.adapter = adapter
        }
    */

    // 개별 삭제 메서드
    /*
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
    */

    /*    // 전체 선택 체크박스 메소드
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

        }*/
}

