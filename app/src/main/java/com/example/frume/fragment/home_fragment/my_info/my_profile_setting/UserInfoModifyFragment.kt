package com.example.frume.fragment.home_fragment.my_info.my_profile_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserInfoModifyBinding


// sehoon 유저 정보 수정 화면
class UserInfoModifyFragment : Fragment() {
    private var _binding: FragmentUserInfoModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_modify, container, false)
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

    // sehoon 여기에 모든 메서드 넣어주세요
    private fun setLayout() {
        onClickButtonSubmit()
    }

    // sehoon 저장버튼 클릭 메서드
    // 나의 배송지 화면으로 돌아감
    private fun onClickButtonSubmit() {
        binding.buttonUserInfoManageModifyUserInfo.setOnClickListener {
            //val action = UserInfoModifyFragmentDirections.actionUserInfoModifyToUserInfoManage()
            findNavController().navigateUp()
        }
    }

}