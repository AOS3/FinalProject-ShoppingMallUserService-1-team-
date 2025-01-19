package com.example.frume.fragment.home_fragment.my_info.order_inquiry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.data_hj.DummyData
import com.example.frume.databinding.FragmentUserOrderHistoryBinding
import com.example.frume.databinding.ItemProductOrderBinding
import com.google.android.material.divider.MaterialDividerItemDecoration


class UserOrderHistoryFragment() : Fragment() {

    lateinit var userOrderHistoryBinding : FragmentUserOrderHistoryBinding
    lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userOrderHistoryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_order_history,container,false)
        homeActivity = activity as HomeActivity

        // 주문 상태 드롭다운 메뉴 실행
        setupOrderStateDropdown()
        // recyclerView 설정
        settingRecyclerView()
        return userOrderHistoryBinding.root
    }

    // 주문 상태 드롭다운 메뉴 버튼 리스너
    private fun setupOrderStateDropdown() {
        val autoCompleteOrderStateTextView = userOrderHistoryBinding.autoCompleteTextViewUserOrderHistoryOrderState
        val autoCompleteSearchPeriodTextView= userOrderHistoryBinding.autoCompleteTextViewUserOrderHistorySearchPeriod
        // 드롭다운 데이터 정의
        val orderStates = listOf("전체", "출고 준비중", "출고 완료", "배송 준비중", "배송 완료", "결제 대기중", "결제 완료", "취소", "반품"," 교환")

        val searchPeriods = listOf("15일", "1개월", "3개월", "6개월")


        // ArrayAdapter 생성 (autoCompleteOrderStateTextView에 데이터를 연결)
        val adapterOrderState = ArrayAdapter(
            homeActivity,
            android.R.layout.simple_dropdown_item_1line,
            orderStates
        )

        // ArrayAdapter 생성 (autoCompleteSearchPeriodTextView에 데이터를 연결)
        val adapterSearchPeriodState = ArrayAdapter(
            homeActivity,
            android.R.layout.simple_dropdown_item_1line,
            searchPeriods
        )

        // autoCompleteOrderStateTextView에 어댑터 연결
        autoCompleteOrderStateTextView.setAdapter(adapterOrderState)

        // autoCompleteSearchPeriodTextView에 어댑터 연결
        autoCompleteSearchPeriodTextView.setAdapter(adapterSearchPeriodState)

        // autoCompleteOrderStateTextView 항목 선택 이벤트 리스너 설정
        autoCompleteOrderStateTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedOrderState = parent.getItemAtPosition(position).toString()
            // 선택된 항목 처리
            Toast.makeText(requireContext(), "선택된 상태: $selectedOrderState", Toast.LENGTH_SHORT).show()
        }

        // autoCompleteSearchPeriodTextView에 항목 선택 이벤트 리스너 설정
        autoCompleteSearchPeriodTextView.setOnItemClickListener{parent, view, position, id ->
            val selectedSearchPeriod = parent.getItemAtPosition(position).toString()
            // 선택된 항목 처리
            Toast.makeText(requireContext(), "선택된 상태: $selectedSearchPeriod", Toast.LENGTH_SHORT).show()
        }
    }



    // RecyclerView를 구성하는 메서드
         fun settingRecyclerView(){
         userOrderHistoryBinding.apply {
                 // 어뎁터
                 recyclerViewUserOrderHistory.adapter = RecyclerViewOrderHistoryAdapter()
                 // LayoutManager
                 recyclerViewUserOrderHistory.layoutManager = LinearLayoutManager(homeActivity)
                 // 구분선
                 val deco = MaterialDividerItemDecoration(homeActivity, MaterialDividerItemDecoration.VERTICAL)
                 recyclerViewUserOrderHistory.addItemDecoration(deco)
             }
         }

         // RecyclerView의 어뎁터
         inner class RecyclerViewOrderHistoryAdapter : RecyclerView.Adapter<RecyclerViewOrderHistoryAdapter.ViewHolderMain>(){
             // ViewHolder
             inner class ViewHolderMain(val itemProductOrderBinding: ItemProductOrderBinding) : RecyclerView.ViewHolder(itemProductOrderBinding.root),
                 View.OnClickListener {
                 override fun onClick(v: View?) {
                     // 사용자가 누른 동물 인덱스 담아준다.
                     val dataBundle = Bundle()
                    // dataBundle.putInt("animalIdx", testList[adapterPosition].)
                     // ShowFragment로 이동한다.
                   //  mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, dataBundle)
                 }
             }

             override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {

                 val itemProductOrderBinding = ItemProductOrderBinding.inflate(layoutInflater, parent, false)

                 val viewHolderItemOrderHistory = ViewHolderMain(itemProductOrderBinding)

                 // 리스너를 설정해준다.
                 itemProductOrderBinding.root.setOnClickListener(viewHolderItemOrderHistory)

                 return viewHolderItemOrderHistory
             }

             override fun getItemCount(): Int {
                 return DummyData.dummyShippingItems.size
             }

             override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
                 holder.itemProductOrderBinding.textViewItemProductOrderOrderStatus.text = DummyData.dummyShippingItems[position].deliverState
                 holder.itemProductOrderBinding.textViewItemProductOrderProductName.text = DummyData.dummyShippingItems[position].productName
                 holder.itemProductOrderBinding.imageViewItemProductOrderProduct.setImageResource(DummyData.dummyShippingItems[position].imgPath)
             }
         }

}