package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserAddressAddBinding


class UserAddressAddFragment : Fragment() {
    private var _binding: FragmentUserAddressAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_address_add, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLayout()
    }

    private fun setLayout() {
        onClickToolbar()
        onClickConfirmBtn()
    }

    // sehoon 툴바 클릭 메서드
    private fun onClickToolbar() {
        binding.toolBarUserAddressModify.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // sehoon 저장버튼 클릭 메서드
    private fun onClickConfirmBtn() {
        binding.buttonUserAddressModifyArrivalAdd.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}