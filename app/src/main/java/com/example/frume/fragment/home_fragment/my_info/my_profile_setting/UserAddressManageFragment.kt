package com.example.frume.fragment.home_fragment.my_info.my_profile_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data_ye.DummyData
import com.example.frume.data_ye.TempAddress
import com.example.frume.databinding.FragmentUserAddressManageBinding
import com.example.frume.databinding.ItemDeliverySpotBinding

// sehoon 내 배송지 관리 화면
class UserAddressManageFragment : Fragment() {
    private var _binding: FragmentUserAddressManageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_address_manage, container, false)
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
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewUserAddressManage() {
        binding.apply {
            // 더미 데이터를 이용하여 RecyclerView의 어댑터에 주소 리스트 전달
            recyclerViewUserOrderHistory.adapter = RecyclerViewUserAddressManageAdapter(DummyData.AddressList)
        }
    }

    // 배송지 추가 버튼 클릭 메서드
    private fun onClickAddressAddBtn() {
        binding.toolBarUserAddressModify.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                // 배송지 추가 메뉴 버튼 클릭 시
                R.id.menu_user_address_manage_add -> {
                    // 배송지 추가 화면으로 네비게이션
                    val action = UserAddressManageFragmentDirections.actionUserAddressManageToUserAddressAdd()
                    findNavController().navigate(action)
                    true
                }
                else -> true
            }
        }
    }

    // 네비게이션 클릭 메서드 (툴바의 뒤로가기 버튼)
    private fun onClickToolbar() {
        binding.toolBarUserAddressModify.setNavigationOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 화면으로 네비게이션
            findNavController().navigateUp()
        }
    }

    // RecyclerView의 어댑터
    inner class RecyclerViewUserAddressManageAdapter(private val addressList: List<TempAddress>) : RecyclerView.Adapter<RecyclerViewUserAddressManageAdapter.ViewHolderUserAddress>() {

        // ViewHolder (주소 항목을 바인딩할 뷰 홀더)
        inner class ViewHolderUserAddress(val binding: ItemDeliverySpotBinding) : RecyclerView.ViewHolder(binding.root)

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
                // 주소 이름, 상세 주소, 우편번호, 아이콘 리소스를 각각 바인딩
                addressName.text = item.addressName
                addressDetail.text = item.addressDetail
                postalCode.text = item.postalCode
                addressIcon.setImageResource(item.addressIconResId)
            }
        }
    }
}




