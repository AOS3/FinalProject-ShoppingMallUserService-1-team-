package com.example.frume.fragment.my_info.order_inquiry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frume.R
import com.example.frume.activity.HomeActivity
import com.example.frume.databinding.FragmentUserOrderHistoryBinding
import com.example.frume.databinding.ItemProductOrderBinding
import com.example.frume.model.OrderProductModel
import com.example.frume.service.OrderProductService
import com.example.frume.service.OrderService
import com.example.frume.util.OrderSearchPeriod
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class UserOrderHistoryFragment() : Fragment() {

    lateinit var userOrderHistoryBinding: FragmentUserOrderHistoryBinding
    lateinit var homeActivity: HomeActivity

    var showOrderProductList = mutableListOf<OrderProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userOrderHistoryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_order_history,
            container,
            false
        )
        homeActivity = activity as HomeActivity

        // 뒤로가기 메서드 실행
        onClickNavigationIcon()
        // 주문 상태 드롭다운 메뉴 실행
        setupOrderStateDropdown()
        // recyclerView 설정
        settingRecyclerView()
        // List 전체 조회로 가져오기 (매개변수에 조회를 안넣으면 전체조회)
        gettingOrderProductListBySearchPeriod()
        // 날짜 필터를 전체로 조절
        modifyShowingDate()
        return userOrderHistoryBinding.root
    }

    // 뒤로가기 버튼 리스너
    fun onClickNavigationIcon() {
        userOrderHistoryBinding.toolbarUserOrderHistory.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 주문 상태 드롭다운 메뉴 버튼 리스너
    private fun setupOrderStateDropdown() {
        val autoCompleteSearchPeriodTextView =
            userOrderHistoryBinding.autoCompleteTextViewUserOrderHistorySearchPeriod

        // 전체, 15일, 1개월, 3개월, 6개월ㅜ
        val searchPeriods = listOf(OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL.str, OrderSearchPeriod.ORDER_SEARCH_PERIOD_15DAYS.str, OrderSearchPeriod.ORDER_SEARCH_PERIOD_ONE_MONTH.str, OrderSearchPeriod.ORDER_SEARCH_PERIOD_THREE_MONTH.str, OrderSearchPeriod.ORDER_SEARCH_PERIOD_SIX_MONTH.str)


        // ArrayAdapter 생성 (autoCompleteSearchPeriodTextView에 데이터를 연결)
        val adapterSearchPeriodState = ArrayAdapter(
            homeActivity,
            android.R.layout.simple_dropdown_item_1line,
            searchPeriods
        )


        // autoCompleteSearchPeriodTextView에 어댑터 연결
        autoCompleteSearchPeriodTextView.setAdapter(adapterSearchPeriodState)

        // autoCompleteSearchPeriodTextView에 항목 선택 이벤트 리스너 설정
        autoCompleteSearchPeriodTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedSearchPeriod = parent.getItemAtPosition(position).toString()
            // 선택된 항목 처리

            //토스트 생성
            Toast.makeText(requireContext(), "선택된 상태: $selectedSearchPeriod", Toast.LENGTH_SHORT)
                .show()

            // 해당 목록에 따라, 날짜와, 리스트를 변경한다.
            when(selectedSearchPeriod){
                OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL.str->{
                    gettingOrderProductListBySearchPeriod(OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL)
                    modifyShowingDate(selectedSearchPeriod)
                }
                OrderSearchPeriod.ORDER_SEARCH_PERIOD_15DAYS.str->{
                    gettingOrderProductListBySearchPeriod(OrderSearchPeriod.ORDER_SEARCH_PERIOD_15DAYS)
                    modifyShowingDate(selectedSearchPeriod)
                }
                OrderSearchPeriod.ORDER_SEARCH_PERIOD_ONE_MONTH.str->{
                    gettingOrderProductListBySearchPeriod(OrderSearchPeriod.ORDER_SEARCH_PERIOD_ONE_MONTH)
                    modifyShowingDate(selectedSearchPeriod)
                }
                OrderSearchPeriod.ORDER_SEARCH_PERIOD_THREE_MONTH.str->{
                    gettingOrderProductListBySearchPeriod(OrderSearchPeriod.ORDER_SEARCH_PERIOD_THREE_MONTH)
                    modifyShowingDate(selectedSearchPeriod)
                }
                else->{
                    gettingOrderProductListBySearchPeriod(OrderSearchPeriod.ORDER_SEARCH_PERIOD_SIX_MONTH)
                    modifyShowingDate(selectedSearchPeriod)
                }
            }
        }
    }

    fun refreshRecyclerView() {
        userOrderHistoryBinding.apply {
            recyclerViewUserOrderHistory.adapter?.notifyDataSetChanged()
        }
    }


    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView() {
        userOrderHistoryBinding.apply {
            // 어뎁터
            recyclerViewUserOrderHistory.adapter = RecyclerViewOrderHistoryAdapter()
            // LayoutManager
            recyclerViewUserOrderHistory.layoutManager = LinearLayoutManager(homeActivity)
            // 구분선
            val deco =
                MaterialDividerItemDecoration(homeActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewUserOrderHistory.addItemDecoration(deco)
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewOrderHistoryAdapter :
        RecyclerView.Adapter<RecyclerViewOrderHistoryAdapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(val itemProductOrderBinding: ItemProductOrderBinding) :
            RecyclerView.ViewHolder(itemProductOrderBinding.root),
            View.OnClickListener {
            override fun onClick(v: View?) {

                // 주문 상세 내역(USerOrderHistoryFragment)으로 이동
                val action =
                    UserOrderHistoryFragmentDirections.actionUserOrderHistoryToUserOrderDetail(showOrderProductList[adapterPosition].orderProductDocId,showOrderProductList[adapterPosition].orderId,)
                findNavController().navigate(action)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val itemProductOrderBinding =
                ItemProductOrderBinding.inflate(layoutInflater, parent, false)
            val viewHolderItemOrderHistory = ViewHolderMain(itemProductOrderBinding)
            // 리스너를 설정해준다.
            itemProductOrderBinding.root.setOnClickListener(viewHolderItemOrderHistory)
            return viewHolderItemOrderHistory
        }

        override fun getItemCount(): Int {
            return showOrderProductList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
             holder.itemProductOrderBinding.textViewItemProductOrderOrderStatus.text = showOrderProductList[position].orderState.str
             holder.itemProductOrderBinding.textViewItemProductOrderProductName.text = showOrderProductList[position].orderProductName
            Glide.with(holder.itemProductOrderBinding.imageViewItemProductOrderProduct.context)
                .load(showOrderProductList[position].orderProductImagePath)
                .into(holder.itemProductOrderBinding.imageViewItemProductOrderProduct)


        }
    }

    // 리스트 가져오기
    // 기본값은 전체조회
    fun gettingOrderProductListBySearchPeriod(orderSearchPeriod: OrderSearchPeriod = OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL) {
        showOrderProductList.removeAll(showOrderProductList)
        CoroutineScope(Dispatchers.Main).launch {
            val orderModelList = withContext(Dispatchers.IO) {
                OrderService.gettingMyOrder(homeActivity.loginUserDocumentId)
            }
            val orderDocIdList = orderModelList.map { it.orderDocId }

            val gettingOrderProductList = withContext(Dispatchers.IO) {
                OrderProductService.gettingMyOrderProductItems(orderDocIdList, orderSearchPeriod)
            }
            showOrderProductList = gettingOrderProductList
            refreshRecyclerView()
        }
    }

    // 입력된 날짜 정렬날짜 기준으로 시작날짜와 현재날짜를 구한다.
    fun modifyShowingDate(dateString: String=OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL.str) {
       userOrderHistoryBinding.apply {
           when (dateString) {
               OrderSearchPeriod.ORDER_SEARCH_PERIOD_ALL.str->{
                   // 현재 날짜
                   val currentDate = Timestamp.now()

                   textViewUserOrderHistoryFilteredFrontPeriod.text = ""
                   textViewFilteredBackPeriod.text = convertToDate(currentDate)
               }
               OrderSearchPeriod.ORDER_SEARCH_PERIOD_15DAYS.str->{
                   // 현재 날짜를 기준으로 Calendar 인스턴스 생성
                   val calendar = Calendar.getInstance()

                   // 15일 전으로 설정
                   calendar.add(Calendar.DAY_OF_YEAR, -15)

                   // Calendar를 Timestamp로 변환
                   val daysAgo15 = Timestamp(calendar.time)

                   // 현재 날짜
                   val currentDate = Timestamp.now()

                   textViewUserOrderHistoryFilteredFrontPeriod.text = convertToDate(daysAgo15)
                   textViewFilteredBackPeriod.text = convertToDate(currentDate)
               }
               OrderSearchPeriod.ORDER_SEARCH_PERIOD_ONE_MONTH.str->{
                   // 현재 날짜를 기준으로 Calendar 인스턴스 생성
                   val calendar = Calendar.getInstance()

                   // 1달 전으로 설정
                   calendar.add(Calendar.MONTH, -1)

                   // Calendar를 Timestamp로 변환
                   val daysAgo15 = Timestamp(calendar.time)

                   // 현재 날짜
                   val currentDate = Timestamp.now()

                   textViewUserOrderHistoryFilteredFrontPeriod.text = convertToDate(daysAgo15)
                   textViewFilteredBackPeriod.text = convertToDate(currentDate)
               }
               OrderSearchPeriod.ORDER_SEARCH_PERIOD_THREE_MONTH.str->{
                   // 현재 날짜를 기준으로 Calendar 인스턴스 생성
                   val calendar = Calendar.getInstance()

                   // 1달 전으로 설정
                   calendar.add(Calendar.MONTH, -3)

                   // Calendar를 Timestamp로 변환
                   val daysAgo15 = Timestamp(calendar.time)

                   // 현재 날짜
                   val currentDate = Timestamp.now()

                   textViewUserOrderHistoryFilteredFrontPeriod.text = convertToDate(daysAgo15)
                   textViewFilteredBackPeriod.text = convertToDate(currentDate)
               }
               else->{
                   // 현재 날짜를 기준으로 Calendar 인스턴스 생성
                   val calendar = Calendar.getInstance()

                   // 1달 전으로 설정
                   calendar.add(Calendar.MONTH, -6)

                   // Calendar를 Timestamp로 변환
                   val daysAgo15 = Timestamp(calendar.time)

                   // 현재 날짜
                   val currentDate = Timestamp.now()

                   textViewUserOrderHistoryFilteredFrontPeriod.text = convertToDate(daysAgo15)
                   textViewFilteredBackPeriod.text = convertToDate(currentDate)
               }
           }
       }
    }

     // timeStamp -> String 변환
         private fun convertToDate(timeStamp: Timestamp): String {
             // Firestore Timestamp를 Date 객체로 변환
             val date = timeStamp.toDate()

             // 한국 시간대 (Asia/Seoul)로 설정
             val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
             dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

             return dateFormat.format(date)
         }
}