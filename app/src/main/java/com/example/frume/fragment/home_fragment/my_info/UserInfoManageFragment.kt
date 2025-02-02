package com.example.frume.fragment.home_fragment.my_info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.activity.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoManageBinding
import com.example.frume.activity.LoginActivity
import com.example.frume.model.UserModel
import com.example.frume.service.UserService
import com.example.frume.util.CustomerUserState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserInfoManageFragment : Fragment() {
    private var _binding: FragmentUserInfoManageBinding? = null
    private val binding get() = _binding!!

    lateinit var homeActivity: HomeActivity

    // 사용자 정보를 담을 변수
    lateinit var userModel: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeActivity = activity as HomeActivity

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_manage, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickButtonUserInfoModify()
        onClickButtonModifyPw()
        onClickToolbar()
        onClickButtonUserInfoManageWithdrawal()  // 탈퇴하기 버튼 클릭 리스너 추가
        settingInputData()
    }

    // sehoon 정보 수정하기 클릭 메서드
    private fun onClickButtonUserInfoModify() {
        binding.buttonUserInfoManageModifyUserInfo.setOnClickListener {
            val action = UserInfoManageFragmentDirections.actionUserInfoManageToUserInfoModifyFragment()
            findNavController().navigate(action)
        }
    }

    // sehoon 비밀번호 변경 클릭 메서드
    private fun onClickButtonModifyPw() {
        binding.textViewUserInfoManageModifyPW.setOnClickListener {
            val action = UserInfoManageFragmentDirections.actionUserInfoManageToUserPwModify()
            findNavController().navigate(action)
        }
    }

    // sehoon 툴바 클릭 메서드
    private fun onClickToolbar() {
        binding.toolbarUserInfoModify.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 탈퇴하기 버튼 클릭 메서드
    private fun onClickButtonUserInfoManageWithdrawal() {
        binding.buttonUserInManageWithdrawal.setOnClickListener {
            // 탈퇴 확인 다이얼로그 표시
            showWithdrawalDialog()
        }
    }

    // 탈퇴 확인 다이얼로그 표시
    private fun showWithdrawalDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("계정 탈퇴")
            .setMessage("정말로 계정을 탈퇴하시겠습니까? 회원 탈퇴 시 로그인할 수 없습니다")
            .setPositiveButton("확인") { dialog, which ->
                // 확인 클릭 시 계정 탈퇴 처리
                performAccountWithdrawal()
            }
            .setNegativeButton("취소") { dialog, which ->
                // 취소 클릭 시 다이얼로그 닫기
                dialog.dismiss()
            }
            .show()
    }

    // 계정 탈퇴 처리 함수
    private fun performAccountWithdrawal() {

        CoroutineScope(Dispatchers.Main).launch {

            // 회원 상태 변경
            val work1 = async(Dispatchers.IO){
                //  UserService.updateUserState(boardActivity.loginUserDocumentId, UserState.USER_STATE_SIGNOUT)
                UserService.updateUserState(homeActivity.loginUserDocumentId,CustomerUserState.CUSTOMER_USER_STATE_WITHDRAWN)
            }
            work1.join()

            // 토큰 삭제
            val sharedPreferences = requireContext().getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("token") // 저장된 토큰 삭제
            editor.apply()

            // 탈퇴 후 로그인 화면으로 이동
            val intent = Intent(homeActivity, LoginActivity::class.java)
            startActivity(intent)

            // 현재 액티비티 종료
            homeActivity.finish()
        }

        // 탈퇴 완료 메시지
        Toast.makeText(requireContext(), "계정이 탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
    }

    // 데이터를 읽어와 입력 요소를 채워준다.
    fun settingInputData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                //UserService.selectUserDataByUserDocumentIdOne(boardActivity.loginUserDocumentId)
                UserService.selectUserDataByUserDocumentId(homeActivity.loginUserDocumentId)
            }
            userModel = work1.await()

            binding.apply {

                textInputLayoutUserInfoModifyUserName.editText?.setText(userModel?.customerUserName ?: "")
                textInputLayoutUserInfoModifyPhoneNumber.editText?.setText(userModel?.customerUserPhoneNumber ?: "")
                textViewUserInfoModifyShowAddress.text = userModel.customerUserAddress["BasicAddress"]
                textViewUserInfoModifyShowPostNumber.text = userModel.customerUserAddress["PostNumber"]
                textInputLayoutUserInfoModifyDetailAddress.editText?.setText(userModel?.customerUserAddress?.get("DetailedAddress") ?: "")

            }

        }
    }
}
