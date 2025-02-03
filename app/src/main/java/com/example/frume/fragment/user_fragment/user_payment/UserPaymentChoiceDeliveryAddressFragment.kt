package com.example.frume.fragment.user_fragment.user_payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.databinding.FragmentUserPaymentChoiceDeliveryAddressBinding
import com.example.frume.databinding.ItemDeliverySpotBinding
import com.example.frume.activity.HomeActivity
import com.example.frume.fragment.user_fragment.product_info.UserProductInfoDialogFragmentDirections
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.service.UserDeliveryAddressService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserPaymentChoiceDeliveryAddressFragment : Fragment() {
    private val args: UserPaymentChoiceDeliveryAddressFragmentArgs by navArgs()
    lateinit var homeActivity: HomeActivity
    private var _binding: FragmentUserPaymentChoiceDeliveryAddressBinding? = null
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_payment_choice_delivery_address, container, false)
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
        // 네비게이션 클릭 메서드 (툴바의 주소 추가 버튼)
        onClickToolbarMenu()
    }

    override fun onResume() {
        super.onResume()
        // 리사이클러뷰 어뎁터 설정, 통신으로 DB 에서 리스트 가져오기
        gettingAddressList(homeActivity.loginUserDocumentId)
    }

    // 항상 기본배송지가 [0]에 가도록 하기
    fun settingUpToDefaultSpotAddressList() {
        // 기본 배송지를 찾은 후, 해당 항목을 첫 번째 인덱스로 이동
        val defaultAddress = addressList.find { it.deliveryAddressIsDefaultAddress.bool }

        // 기본 배송지가 존재하면
        defaultAddress?.let { address ->
            // 기본 배송지 항목을 리스트에서 제거하고 첫 번째 인덱스로 추가
            addressList.remove(address)
            addressList.add(0, address)

            // 변경된 리스트를 어댑터에 반영
            binding.recyclerUserCartChoiceDeliveryAddress.adapter?.notifyDataSetChanged()
        }
    }

    // 배송지 목록 가져오는 메서드
    fun gettingAddressList(userDocId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                // 데이터 로드
                addressList = UserDeliveryAddressService.gettingDeliveryAddressList(userDocId)
            }
            work1.await()

            // 디버깅 로그
            Log.d("test10", "UserCartChoiceDeliveryAddressFragment -> gettingAddressList() : $addressList")

            // 항상 기본배송지가 [0]에 가도록 하는 메서드
            settingUpToDefaultSpotAddressList()

            // 어댑터 새로 생성 후 RecyclerView에 설정
            settingRecyclerViewUserAddressManage()
        }
    }



    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewUserAddressManage() {
        binding.recyclerUserCartChoiceDeliveryAddress.apply {
            layoutManager = LinearLayoutManager(context) // 세로 방향 LinearLayoutManager 설정
            adapter = RecyclerViewUserChoiceAddressManageAdapter(addressList) // 어댑터 설정
        }
    }

    // 네비게이션 클릭 메서드 (툴바의 뒤로가기 버튼)
    private fun onClickToolbar() {
        binding.toolbarUserCartChoiceDeliveryAddress.setNavigationOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 화면으로 네비게이션
            findNavController().navigateUp()
        }
    }

    // 네비게이션 클릭 메서드 (툴바의 주소 추가 버튼)
    private fun onClickToolbarMenu() {
        binding.toolbarUserCartChoiceDeliveryAddress.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_user_payment_choice_delivery_address_add->{
                    val action= UserPaymentChoiceDeliveryAddressFragmentDirections.actionUserCartChoiceDeliverAddressToUserAddressAdd()
                    findNavController().navigate(action)
                    true
                }

                else -> {true}
            }
        }
    }

    // RecyclerView의 어댑터
    inner class RecyclerViewUserChoiceAddressManageAdapter(private val addressList: MutableList<DeliveryAddressModel>) : RecyclerView.Adapter<RecyclerViewUserChoiceAddressManageAdapter.ViewHolderUserChoiceAddress>() {

        // ViewHolder
        inner class ViewHolderUserChoiceAddress(val binding: ItemDeliverySpotBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                // 항목 클릭 리스너 설정
                binding.root.setOnClickListener {
                    // 배송지로 할것인가 다이얼로그
                    val selectedAddressModel = addressList[adapterPosition]

                    homeActivity.showConfirmationDialog("배송지 변경","${selectedAddressModel.deliveryAddressName}로 배송지를 변경하시겠습니다?","네","아니요",fun (){
                        val navOption = NavOptions.Builder()
                            .setPopUpTo(R.id.user_payment_screen, inclusive = true)
                            .build()

                        val action = UserPaymentChoiceDeliveryAddressFragmentDirections.actionUserCartChoiceDeliverAddressToUserPaymentScreen(selectedAddressModel.deliveryAddressDocId,args.fromWhere,args.productDocIdDirectPurchase,args.dueDateDirectPurchase,args.deliverySubscribeStateDirectPurchase, args.productCountDirectPurchase)
                        findNavController().navigate(action,  navOption)

                    })
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
                // postalCode.text = item.deliveryAddressPostNumber

                // 기본 배송지 값이 true 일 때
                if(item.deliveryAddressIsDefaultAddress.bool){
                    addressIcon.visibility = View.VISIBLE // 보이게 설정
                }
            }
        }
    }

}