package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoBinding

class UserInfoFragment() : Fragment() {

    lateinit var homeActivity: HomeActivity
    lateinit var fragmentUserInfoBinding: FragmentUserInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentUserInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false)
        homeActivity = activity as HomeActivity
        // 주문 내역 리스너 실행
        onClickOrderHistory()
        // 배송지 관리 리스너 실행
        onClickDeliverySpotManagement()
        // 회원 관리 및 탈퇴 리스너 실행
        onClickUserInfoManagementOrLeave()
        // 후기 리스너 실행
        onClickTextViewUserReview()
        return fragmentUserInfoBinding.root
    }

    // 주문 내역 리스너
    fun onClickOrderHistory() {
        fragmentUserInfoBinding.textViewUserInfoOrderHistory.setOnClickListener {
            val action = UserInfoFragmentDirections.actionNavigationProfileToUserOrderHistory()
            findNavController().navigate(action)
        }
    }

    // 배송지 관리 리스너
    fun onClickDeliverySpotManagement() {
        fragmentUserInfoBinding.textViewUserInfoShippingInfo.setOnClickListener {
            val action = UserInfoFragmentDirections.actionNavigationProfileToUserAddressManageFragment()
            findNavController().navigate(action)
        }
    }

    // 후기 텍스트뷰 클릭 리스너
    fun onClickTextViewUserReview() {
        fragmentUserInfoBinding.TextViewUserInfoReview.setOnClickListener {

        }
    }

    // 회원 정보 및 탈퇴 리스너
    fun onClickUserInfoManagementOrLeave() {
        fragmentUserInfoBinding.textViewUserInfoAccountInfo.setOnClickListener {
            val action = UserInfoFragmentDirections.actionNavigationProfileToUserInfoManage()
            findNavController().navigate(action)
        }

    }


}