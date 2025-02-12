package com.example.frume.fragment.user_info

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
import com.example.frume.R
import com.example.frume.activity.HomeActivity
import com.example.frume.activity.LoginActivity
import com.example.frume.databinding.FragmentUserInfoBinding
import com.example.frume.model.UserModel
import com.example.frume.service.ReviewService
import com.example.frume.service.UserService
import com.example.frume.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
        // 유저 정보 가져오는 메서드 호출
        getUserInfo()
        // 내 구독 정보 클릭 메서드 호출
        onClickTextViewSubscription()
        // 상품 문의 내역 클릭 메서드 호출
        onClickTextViewInquiry()
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

    private fun getUserInfo() {
        CoroutineScope(Dispatchers.Main).launch {
            var userModel = UserModel()

            // 리뷰 개수 가져오는 비동기 작업
            val work0 = async(Dispatchers.IO) {
                ReviewService.getUserReviewCount(homeActivity.loginUserDocumentId)
            }
            val reviewCnt = work0.await() // 리뷰 개수 받기

            // 리뷰 개수 로그 출력
            Log.d("test200", "getUserInfo에서 받은 리뷰 개수: $reviewCnt")

            val work1 = async(Dispatchers.IO){
                UserService.getUserInfo(homeActivity.loginUserDocumentId)
            }
            val  userModelList = work1.await()

            // 유저 정보가 비어 있는지 확인
            if (userModelList.isNotEmpty()) {
                userModel = userModelList[0]  // 첫 번째 유저 정보 사용

                fragmentUserInfoBinding.textViewTitleUserInfo.text = "${userModel.customerUserName}님"

                fragmentUserInfoBinding.reWardCostTextViewUserInfo.text = "${userModel.customerUserReward} 원"

                fragmentUserInfoBinding.reviewCostTextViewUserInfo.text = "${reviewCnt} 건"

                // 회원일 때 텍스트뷰 활성화
                enableAccountInfo(true)
            } else {
                Log.d("test100", "유저 정보를 찾을 수 없습니다. 기본 값을 사용합니다.")

                fragmentUserInfoBinding.textViewTitleUserInfo.text = "비회원님"

                fragmentUserInfoBinding.reWardCostTextViewUserInfo.text = "X"

                fragmentUserInfoBinding.reviewCostTextViewUserInfo.text = "X"

                // 비회원일 때 텍스트뷰 비활성화
                enableAccountInfo(false)
            }


        }
    }

    // 회원 정보 관리 및 탈퇴 + 로그아웃 텍스트뷰 활성화/비활성화 메서드
    private fun enableAccountInfo(enable: Boolean) {
        fragmentUserInfoBinding.textViewUserInfoAccountInfo.apply {
            isEnabled = enable  // 클릭 가능 여부 설정
            setTextColor(
                if (enable) resources.getColor(R.color.black, null)  // 활성화 시 검정색
                else resources.getColor(R.color.gray100, null)  // 비활성화 시 회색
            )
        }

        fragmentUserInfoBinding.textViewLogoutUserInfo.apply {
            isEnabled = enable  // 클릭 가능 여부 설정
            setTextColor(
                if (enable) resources.getColor(R.color.black, null)  // 활성화 시 검정색
                else resources.getColor(R.color.gray100, null)  // 비활성화 시 회색
            )

        }
    }
    // 상품 문의 내역 클릭 메서드
    private fun onClickTextViewInquiry() {
        fragmentUserInfoBinding.textViewUserInfoProductInquiry.setOnClickListener {
            requireContext().showToast("추후 구현 예정")
        }
    }

    // 내 구독 정보 클릭 메서드
    private fun onClickTextViewSubscription() {
        fragmentUserInfoBinding.textViewInfoUserInfoSubscription.setOnClickListener {
            requireContext().showToast("추후 구현 예정")
        }
    }



}