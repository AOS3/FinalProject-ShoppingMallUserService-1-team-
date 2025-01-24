package com.example.frume.fragment.user_fragment.user_cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCartChoiceDeliveryAddressBinding
import com.example.frume.databinding.ItemDeliverySpotBinding
import com.example.frume.home.HomeActivity
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.service.UserDeliveryAddressService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserCartChoiceDeliveryAddressFragment : Fragment() {

    lateinit var homeActivity: HomeActivity
    private var _binding: FragmentUserCartChoiceDeliveryAddressBinding? = null
    private val binding get() = _binding!!

    // 회원 배송지 목록 리스트
    var addressList = mutableListOf<DeliveryAddressModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivity = activity as HomeActivity
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_cart_choice_delivery_address, container, false)
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

    // 화면 구성 메서드 (RecyclerView, 버튼 클릭 등 설정)
    private fun setLayout() {
        // 툴바 네비게이션 클릭 리스너 설정
        onClickToolbar()
        // 주소지 목록 가져오기
        gettingAddressList(homeActivity.loginUserDocumentId)
        // RecyclerView 설정
        settingRecyclerViewUserAddressManage()
    }

    // addressList 만들기
    fun gettingAddressList(userDocId : String) {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                addressList = UserDeliveryAddressService.gettingDeliveryAddressList(userDocId)
            }
            work1.join()
            Log.d("test10","UserCartChoiceDeliveryAddressFragment -> gettingAddressList() : ${addressList}")
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewUserAddressManage() {
        binding.apply {
            // 더미 데이터를 이용하여 RecyclerView의 어댑터에 주소 리스트 전달
            recyclerUserCartChoiceDeliveryAddress.adapter = RecyclerViewUserChoiceAddressManageAdapter(addressList)
        }
    }


    // 네비게이션 클릭 메서드 (툴바의 뒤로가기 버튼)
    private fun onClickToolbar() {
        binding.toolbarUserCartChoiceDeliveryAddress.setNavigationOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 화면으로 네비게이션
            findNavController().navigateUp()
        }
    }

    // RecyclerView의 어댑터
    inner class RecyclerViewUserChoiceAddressManageAdapter(private val addressList: MutableList<DeliveryAddressModel>) : RecyclerView.Adapter<RecyclerViewUserChoiceAddressManageAdapter.ViewHolderUserChoiceAddress>() {

        // ViewHolder
        inner class ViewHolderUserChoiceAddress(val binding: ItemDeliverySpotBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                // 항목 클릭 리스너 설정
                binding.root.setOnClickListener {
                    // 기본배송지로 할것인가 다이얼로그
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserChoiceAddress {
            // 아이템 레이아웃을 바인딩하여 ViewHolder 생성
            val binding = ItemDeliverySpotBinding.inflate(layoutInflater, parent, false)
            return ViewHolderUserChoiceAddress(binding)
        }

        override fun getItemCount(): Int {
            // RecyclerView의 항목 개수 (주소 리스트의 크기 반환)
            return addressList.size
        }

        override fun onBindViewHolder(holder: ViewHolderUserChoiceAddress, position: Int) {
            // 현재 항목의 주소 데이터를 가져와서 뷰에 바인딩
            val item = addressList[position]

            holder.binding.apply {
                // 주소 이름, 상세 주소, 우편번호, 아이콘 리소스를 각각 바인딩
                addressName.text = item.deliveryAddressName
                addressDetail.text = item.deliveryAddressBasicAddress +" ${item.deliveryAddressDetailAddress}"
                postalCode.text = item.deliveryAddressPostNumber
            }
        }
    }

}