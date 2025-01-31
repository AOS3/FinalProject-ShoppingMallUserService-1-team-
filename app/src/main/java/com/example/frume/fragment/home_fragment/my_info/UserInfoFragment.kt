package com.example.frume.fragment.home_fragment.my_info

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoBinding
import com.example.frume.login.LoginActivity

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
        // 로그아웃 리스너 실행
        onClickLogout()
        return fragmentUserInfoBinding.root
    }

    // 로그아웃 클릭 리스너
    fun onClickLogout() {
        fragmentUserInfoBinding.textViewLogoutUserInfo.setOnClickListener {
            showLogoutDialog()
        }
    }

    // 로그아웃 다이얼로그 표시
    private fun showLogoutDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("로그아웃")
            .setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton("확인") { dialogInterface, which ->

                // 토큰 삭제
                val sharedPreferences = requireContext().getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("token") // 저장된 토큰 삭제
                editor.apply()

                Log.d("test100", "저장된 토큰 삭제")

                // 로그아웃 후 다시 로그인 화면으로 이동
                val intent = Intent(homeActivity, LoginActivity::class.java)
                startActivity(intent)

                // 현재 액티비티 종료
                homeActivity.finish()



                Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소") { dialogInterface, which ->
                // 다이얼로그 닫기
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }

    // 주문 내역 리스너
    private fun onClickOrderHistory() {
        fragmentUserInfoBinding.textViewUserInfoOrderHistory.setOnClickListener {
            val action = UserInfoFragmentDirections.actionNavigationProfileToUserOrderHistory()
            findNavController().navigate(action)
        }
    }

    // 배송지 관리 리스너
    private fun onClickDeliverySpotManagement() {
        fragmentUserInfoBinding.textViewUserInfoShippingInfo.setOnClickListener {
            val action = UserInfoFragmentDirections.actionNavigationProfileToUserAddressManageFragment()
            findNavController().navigate(action)
        }
    }

    // 후기 텍스트뷰 클릭 리스너
    private fun onClickTextViewUserReview() {
        fragmentUserInfoBinding.apply {
            TextViewUserInfoReview.setOnClickListener {
                val action = UserInfoFragmentDirections.actionNavigationProfileToUserInfoReviewFragment()
                findNavController().navigate(action)
            }
            reviewCostTextViewUserInfo.setOnClickListener {
                val action = UserInfoFragmentDirections.actionNavigationProfileToUserInfoReviewFragment()
                findNavController().navigate(action)
            }
        }
    }

    // 회원 정보 및 탈퇴 리스너
    private fun onClickUserInfoManagementOrLeave() {
        fragmentUserInfoBinding.textViewUserInfoAccountInfo.setOnClickListener {
            val action = UserInfoFragmentDirections.actionNavigationProfileToUserInfoManage()
            findNavController().navigate(action)
        }

    }



}