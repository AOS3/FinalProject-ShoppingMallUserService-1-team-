package com.example.frume.fragment.user_fragment.user_cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.example.frume.util.CartProductIsCheckStateBoolType
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserCartFragment() : Fragment(), CartClickListener {

    lateinit var fragmentUserCartBinding: FragmentUserCartBinding

    lateinit var homeActivity: HomeActivity

    // private val args: UserCartFragmentArgs by navArgs()
    var cartProductList = mutableListOf<CartProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("test100", "onCreateView")

        homeActivity = activity as HomeActivity

        fragmentUserCartBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_cart, container, false)

        setLayout()

        return fragmentUserCartBinding.root
    }

    private fun setLayout() {
        // 카트 품목을 가져와 카트품목을 구하는 메서드 호출.
        settingCartProductList()
        // UserPaymentScreenFragment로 이동하는 메서드 호출
        onClickCartOrderProduct()
        /*// 배송지 변경 화면으로 이동하는 메서드 호출
        onClickCartDeliverySpotChange()*/
        // RecyclerView 어뎁터 세팅 호출
        settingRecyclerView()
        // 전체 체크박스 리스너 메서드 호출
        onClickCheckBoxAll()
        // 문서 삭제 리스너 메서드 호출
        onClickTextDeleteProducts()

    }

    // 장바구니 item 이 0일때 view
    fun hideView() {
        CoroutineScope(Dispatchers.Main).launch {
            fragmentUserCartBinding.apply {
                if (cartProductList.isEmpty()) {
                    Log.d("hideView", "cartProductList size: ${cartProductList.size}")
                    textViewUserCartDialogPriceLabel.visibility = View.GONE // 숨김
                    buttonUserCartOrder.visibility = View.GONE // 숨김
                    textViewUserCartDialogPrice.visibility = View.GONE // 숨김
                } else {
                    Log.d("hideView", "cartProductList size: ${cartProductList.size}")
                    textViewUserCartDialogPriceLabel.visibility = View.VISIBLE
                    buttonUserCartOrder.visibility = View.VISIBLE
                    textViewUserCartDialogPrice.visibility = View.VISIBLE
                }
            }
        }
    }

    // 리사이클러뷰 다시 그리기
    private fun refreshRecyclerView() {
        fragmentUserCartBinding.apply {
            recyclerViewUserCart.adapter?.notifyDataSetChanged()
        }
    }

    // 구매하기 버튼 클릭 시, UserPaymentScreenFragment로 이동하는 메서드
    private fun onClickCartOrderProduct() {
        fragmentUserCartBinding.buttonUserCartOrder.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                val work1 =async (Dispatchers.IO){
                    cartProductList.forEach {
                        CartProductService.changeCartProductOption(
                            homeActivity.userCartDocId,
                            it.cartProductDocId,
                            it
                        )
                    }
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

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView() {
        fragmentUserCartBinding.apply {
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
                    recyclerViewUserCart.adapter = RecyclerViewCartAdapter()
                    // LayoutManager
                    recyclerViewUserCart.layoutManager = LinearLayoutManager(homeActivity)
                    // 구분선
                    val deco = MaterialDividerItemDecoration(
                        homeActivity,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                    recyclerViewUserCart.addItemDecoration(deco)

                } catch (e: TimeoutCancellationException) {
                    // 2초 이내에 배송지 정보가 로드되지 않으면 타임아웃 처리
                    // 여기서 타임아웃 후 처리를 할 수 있음 (예: 로딩 실패 메시지 표시)
                    // 어뎁터
                    recyclerViewUserCart.adapter = RecyclerViewCartAdapter()
                }
                // 전체 예상결제 금액 구하는 메서드 호출
                showSumPrice()
                // 장바구니 아이템 없을때 적용하는 메서드 호출
                hideView()
            }
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewCartAdapter :
        RecyclerView.Adapter<RecyclerViewCartAdapter.ViewHolderMain>() {


        // ViewHolder
        inner class ViewHolderMain(val itemCartListBinding: ItemUsercartListBinding) :
            RecyclerView.ViewHolder(itemCartListBinding.root),
            View.OnClickListener {
            override fun onClick(v: View?) {}

            fun bind(cart: CartProductModel) {
                itemCartListBinding.apply {
                    checkboxRecyclerViewSelect.setOnClickListener {
                        onClickItemCheckBox(adapterPosition, cart)
                    }
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val itemProductListBinding =
                ItemUsercartListBinding.inflate(layoutInflater, parent, false)

            val viewHolderProductItem = ViewHolderMain(itemProductListBinding)

            // 리스너를 설정해준다.
            itemProductListBinding.root.setOnClickListener(viewHolderProductItem)

            itemProductListBinding.textViewRecyclerViewChangeOption.setOnClickListener {
                val cartDocId = cartProductList[viewHolderProductItem.adapterPosition].cartDocId
                val cartProductDocId =
                    cartProductList[viewHolderProductItem.adapterPosition].cartProductDocId
                val action =
                    UserCartFragmentDirections.actionNavigationCartToBottomSheetShowCartOptionFragment(
                        cartDocId,
                        cartProductDocId
                    )
                findNavController().navigate(action)
            }

            return viewHolderProductItem
        }

        override fun getItemCount(): Int {
            return cartProductList.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            // 수량*단가
            val sumPrice =
                cartProductList[position].cartProductUnitPrice * cartProductList[position].cartItemProductQuantity
            // 수량
            val productQuantity = cartProductList[position].cartItemProductQuantity
            // 배송 예정일
            val dueDateTimeStamp = cartProductList[position].cartItemDeliveryDueDate
            // timeStamp->String 변환
            val dueDateToString = convertToDate(dueDateTimeStamp)

            holder.bind(cartProductList[position])
            // holder 설정
            holder.itemCartListBinding.textViewRecyclerViewProductName.text =
                cartProductList[position].cartProductName
            holder.itemCartListBinding.textViewRecyclerViewProductCount.text =
                productQuantity.toString()
            holder.itemCartListBinding.textViewRecyclerViewProductPrice.text = sumPrice.toString()
            holder.itemCartListBinding.checkboxRecyclerViewSelect.isChecked =
                cartProductList[position].cartItemIsCheckState.bool
            holder.itemCartListBinding.TextViewProductDueDate.text = dueDateToString
        }
    }

    // String 객체를 TimeStamp 객체로 전환하는 메서드
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

    // timeStamp 객체를 String으로 전환하는 메서드
    private fun convertToDate(timeStamp: Timestamp): String {
        // Firestore Timestamp를 Date 객체로 변환
        val date = timeStamp.toDate()
        // 원하는 형식으로 날짜 포맷
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        return dateFormat.format(date)
    }

    // 전체 선택/해제 체크박스를 클릭했을 때 호출되는 메서드
    override fun onClickCheckBoxAll() {
        var isCheckedAll = true  // 모든 항목이 선택되었는지 여부를 판단하는 변수

        // cartProductList의 모든 항목을 순회하면서 체크 상태를 확인
        cartProductList.forEach {
            // 하나라도 체크되지 않은 항목이 있으면 전체 선택 상태를 false로 설정
            if (!it.cartItemIsCheckState.bool) {
                isCheckedAll = false
                return@forEach  // 한 항목이라도 체크되지 않으면 더 이상 확인할 필요 없음
            }
        }

        // 전체 선택 체크박스 상태를 isCheckedAll에 맞게 설정
        fragmentUserCartBinding.checkboxUserCartSelectAll.isChecked = isCheckedAll

        // 전체 선택 체크박스를 클릭했을 때의 동작 설정
        fragmentUserCartBinding.checkboxUserCartSelectAll.setOnClickListener {
            fragmentUserCartBinding.checkboxUserCartSelectAll.apply {
                // 체크박스가 선택되면 모든 항목을 선택 상태로 변경
                if (isChecked) {
                    cartProductList.forEach {
                        it.cartItemIsCheckState =
                            CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_TRUE
                    }
                    // RecyclerView 갱신
                    refreshRecyclerView()
                } else {
                    // 체크박스가 선택 해제되면 모든 항목을 해제 상태로 변경
                    cartProductList.forEach {
                        it.cartItemIsCheckState =
                            CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_FALSE
                    }
                    // RecyclerView 갱신
                    refreshRecyclerView()
                }
                // 전체 가격 구하는 메서드 호출
                showSumPrice()

            }
        }
    }


    // item 체크박스 상태변경 리스너
    override fun onClickItemCheckBox(pos: Int, cartProduct: CartProductModel) {
        // 체크 상태 변경
        cartProduct.cartItemIsCheckState = when (cartProduct.cartItemIsCheckState) {
            CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_TRUE -> CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_FALSE
            else -> CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_TRUE
        }

        // 전체 체크된 항목 수 계산
        val checkedCount = cartProductList.count { it.cartItemIsCheckState.bool == true }

        // "전체 선택" 체크박스 상태 업데이트
        fragmentUserCartBinding.checkboxUserCartSelectAll.isChecked =
            (checkedCount == cartProductList.size)

        // 전체 가격 계산 보여주는 메서드 호출
        showSumPrice()
    }

    // 문서 선택 삭제 리스너
    fun onClickTextDeleteProducts() {
        fragmentUserCartBinding.apply {
            textViewButtonUserCartDelete.setOnClickListener {
                homeActivity.showConfirmationDialog(
                    "선택 상품 제거",
                    "선택하신 상품을 제거하시겠습니까?",
                    "네",
                    "",
                    fun() {
                        // 삭제가 성공적으로 완료되면 true를 리턴받는다.
                        val result = deleteCartProductList(homeActivity.userCartDocId)
                        // 전체 삭제하고 다시받아와야 settingCartProductList()에서 데이터를 부를때까지 대기할 수 있음
                        if (result) {
                            cartProductList.removeAll(cartProductList)
                            // DB에서 cartModel List 다시 받아오고 RecyclerView 세팅 메서드
                            settingListAndRecyclerView()
                        }
                    })
            }
        }
    }

    // 선택 삭제 서비스 메서드
    fun deleteCartProductList(cartDocId: String): Boolean {
        // 선택된 모델 List
        val selectedList = mutableListOf<CartProductModel>()
        // 선택된 Model items DocId List
        val selectedProductModelDocIdList = mutableListOf<String>()

        // 체크된 항목 필터링
        cartProductList.forEach {
            if (it.cartItemIsCheckState == CartProductIsCheckStateBoolType.CART_PRODUCT_IS_CHECKED_TRUE) {
                selectedList.add(it)
            }
        }
        selectedList.forEach {
            selectedProductModelDocIdList.add(it.cartProductDocId)
        }

        // 비동기 작업을 동기적으로 처리
        return runBlocking {
            val result = async(Dispatchers.IO) {
                CartProductService.deleteCartProducts(cartDocId, selectedProductModelDocIdList)
            }
            result.await() // 작업 결과 반환
        }
    }

    // 리스트를 다시 가져와서, 리사이클러뷰를 다시 생성함
    fun settingListAndRecyclerView() {
        settingCartProductList()
        settingRecyclerView()
    }

    // 총 결제 예상 금액 구하는 메서드
    fun calculationSumPrice(): Int {
        if (cartProductList.isEmpty()) return 0
        var sumPrice = 0
        cartProductList.forEach {
            if (it.cartItemIsCheckState.bool) {
                sumPrice += it.cartProductPrice
                Log.d("test1", "it.docId : ${it.cartProductDocId}")
                Log.d("test1", "it.price : ${it.cartProductPrice}")
                Log.d("test1", "it.unitPrice : ${it.cartProductUnitPrice}")
                Log.d("test1", "sumPrice : $sumPrice")
            }
        }
        return sumPrice
    }


    // 총결제 예상금액 ui에 그리는 메서드
    fun showSumPrice() {
        val sumPrice = calculationSumPrice()
        // UI 업데이트는 반드시 메인 스레드에서 해야 함
        CoroutineScope(Dispatchers.Main).launch {
            fragmentUserCartBinding.textViewUserCartDialogPrice.text = "${sumPrice} 원"
        }
    }

}
