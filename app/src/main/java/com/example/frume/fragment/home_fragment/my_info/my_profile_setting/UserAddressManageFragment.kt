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

    // sehoon 모든 메서드 여기에 넣어주세요 (화면 구성)
    private fun setLayout() {
        // RecyclerView 설정
        settingRecyclerViewUserAddressManage()
        onClickAddressAddBtn()
        onClickToolbar()
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewUserAddressManage() {
        binding.apply {
            // 더미 데이터를 이용하여 어댑터에 전달
            recyclerViewUserOrderHistory.adapter = RecyclerViewUserAddressManageAdapter(DummyData.getDummyUserAddresses())
        }
    }

    // sehoon 배송지 추가 버튼 메서드
    private fun onClickAddressAddBtn() {
        binding.toolBarUserAddressModify.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.menu_user_address_manage_add -> {
                    val action = UserAddressManageFragmentDirections.actionUserAddressManageToUserAddressAdd()
                    findNavController().navigate(action)
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    // sehoon 네비게이션 클릭 메서드
    private fun onClickToolbar() {
        binding.toolBarUserAddressModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // RecyclerView의 어댑터
    inner class RecyclerViewUserAddressManageAdapter(private val addressList: List<DummyData.UserAddress>) : RecyclerView.Adapter<RecyclerViewUserAddressManageAdapter.ViewHolderUserAddress>() {

        // ViewHolder
        inner class ViewHolderUserAddress(val binding: ItemDeliverySpotBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserAddress {
            val binding = ItemDeliverySpotBinding.inflate(layoutInflater, parent, false)
            return ViewHolderUserAddress(binding)
        }

        override fun getItemCount(): Int {
            return addressList.size
        }

        override fun onBindViewHolder(holder: ViewHolderUserAddress, position: Int) {
            val item = addressList[position]

            // 주소 이름, 상세 주소, 우편번호, 아이콘 리소스 바인딩
            holder.binding.apply {
                addressName.text = item.addressName
                addressDetail.text = item.addressDetail
                postalCode.text = item.postalCode
                addressIcon.setImageResource(item.addressIconResId)
            }
        }

    }
}



