package com.example.frume.fragment.home_fragment.my_info.my_profile_setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.activity.HomeActivity
import com.example.frume.databinding.FragmentUserAddressManageBinding
import com.example.frume.databinding.ItemDeliverySpotBinding
import com.example.frume.fragment.home_fragment.user_home.UserHomeFragmentDirections
import com.example.frume.fragment.user_fragment.user_payment.UserPaymentChoiceDeliveryAddressFragmentArgs
import com.example.frume.fragment.user_fragment.user_payment.UserPaymentChoiceDeliveryAddressFragmentDirections
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.service.UserDeliveryAddressService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// sehoon 내 배송지 관리 화면
class UserAddressManageFragment : Fragment() {
    private var _binding: FragmentUserAddressManageBinding? = null
    private val binding get() = _binding!!
    lateinit var homeActivity: HomeActivity

    var addressList = mutableListOf<DeliveryAddressModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivity = activity as HomeActivity
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_user_address_manage,
                container,
                false
            )
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
        // RecyclerView 설정
        settingRecyclerViewUserAddressManage()
        // 배송지 추가 버튼 클릭 리스너 설정
        onClickAddressAddBtn()
        // 툴바 네비게이션 클릭 리스너 설정
        onClickToolbar()
        gettingAddressList(homeActivity.loginUserDocumentId)
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewUserAddressManage() {
        binding.recyclerViewUserOrderHistory.apply {
            // 더미 데이터를 이용하여 RecyclerView의 어댑터에 주소 리스트 전달
            /*val list = mutableListOf<DeliveryAddressModel>()
            recyclerViewUserOrderHistory.adapter = RecyclerViewUserAddressManageAdapter(list)*/
            layoutManager = LinearLayoutManager(context) // 세로 방향 LinearLayoutManager 설정
            adapter = RecyclerViewUserAddressManageAdapter(addressList)
        }
    }

    // 배송지 추가 버튼 클릭 메서드
    private fun onClickAddressAddBtn() {
        binding.toolBarUserAddressManage.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                // 배송지 추가 메뉴 버튼 클릭 시
                R.id.menu_user_address_manage_add -> {
                    // 배송지 추가 화면으로 네비게이션
                    val action =
                        UserAddressManageFragmentDirections.actionUserAddressManageToUserAddressAdd()
                    findNavController().navigate(action)
                    true
                }

                else -> true
            }
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
            Log.d(
                "test10",
                "UserCartChoiceDeliveryAddressFragment -> gettingAddressList() : $addressList"
            )

            // 항상 기본배송지가 [0]에 가도록 하는 메서드
            settingUpToDefaultSpotAddressList()

            // 어댑터 새로 생성 후 RecyclerView에 설정
            settingRecyclerViewUserAddressManage()
        }
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
            binding.recyclerViewUserOrderHistory.adapter?.notifyDataSetChanged()
        }
    }

    // 네비게이션 클릭 메서드 (툴바의 뒤로가기 버튼)
    private fun onClickToolbar() {
        binding.toolBarUserAddressManage.setNavigationOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 화면으로 네비게이션
            findNavController().navigateUp()
        }
    }

    // RecyclerView의 어댑터
    inner class RecyclerViewUserAddressManageAdapter(private val addressList: List<DeliveryAddressModel>) :
        RecyclerView.Adapter<RecyclerViewUserAddressManageAdapter.ViewHolderUserAddress>() {

        // ViewHolder
        inner class ViewHolderUserAddress(val binding: ItemDeliverySpotBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                // 항목 클릭 리스너 설정
                binding.root.setOnClickListener {
                    // 클릭된 항목의 position을 사용하여 UserAddressModifyFragment로 이동
                    val address = addressList[adapterPosition]
                    val action =
                        UserAddressManageFragmentDirections.actionUserAddressManageToUserAddressModifyFragment() // 여기서 address를 인자로 전달
                    findNavController().navigate(action)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserAddress {
            // 아이템 레이아웃을 바인딩하여 ViewHolder 생성
            val binding = ItemDeliverySpotBinding.inflate(layoutInflater, parent, false)
            return ViewHolderUserAddress(binding)
        }

        override fun getItemCount(): Int {
            // RecyclerView의 항목 개수 (주소 리스트의 크기 반환)
            return addressList.size
        }

        override fun onBindViewHolder(holder: ViewHolderUserAddress, position: Int) {
            // 현재 항목의 주소 데이터를 가져와서 뷰에 바인딩
            val item = addressList[position]

            holder.binding.apply {
                /*               // 주소 이름, 상세 주소, 우편번호, 아이콘 리소스를 각각 바인딩
                addressName.text = item.addressName
                addressDetail.text = item.addressDetail
                postalCode.text = item.postalCode
                addressIcon.setImageResource(item.addressIconResId)*/

                // 주소 이름, 상세 주소, 우편번호, 아이콘 리소스를 각각 바인딩
                addressName.text = item.deliveryAddressName
                addressDetail.text = item.deliveryAddressBasicAddress + " ${item.deliveryAddressDetailAddress}"
                // postalCode.text = item.deliveryAddressPostNumber

                // 기본 배송지 값이 true 일 때
                if (item.deliveryAddressIsDefaultAddress.bool) {
                    addressIcon.visibility = View.VISIBLE // 보이게 설정
                }
            }
        }
    }
}




