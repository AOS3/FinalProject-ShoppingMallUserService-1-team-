package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserAddressModifyBinding

class UserAddressModifyFragment : Fragment() {
    private var _binding: FragmentUserAddressModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_address_modify, container, false)
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
        onClickToolbar()
        onClickConfirmBtn()
    }

    // sehoon 네비게이션 뒤로가기 메서드
    private fun onClickToolbar() {
        binding.toolBarUserAddressModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // sehoon 저장 버튼 메서드
    private fun onClickConfirmBtn() {
        binding.buttonUserAddressModifyArrivalAdd.setOnClickListener {
            findNavController().navigateUp()
        }

    }


}