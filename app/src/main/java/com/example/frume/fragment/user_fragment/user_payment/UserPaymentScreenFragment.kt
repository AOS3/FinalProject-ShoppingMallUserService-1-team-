package com.example.frume.fragment.user_fragment.user_payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserPaymentScreenBinding

class UserPaymentScreenFragment() : Fragment() {

    private var _binding: FragmentUserPaymentScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_payment_screen, container, false)

        // 네비게이션 아이콘 클릭 리스너 설정
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickToolbarNavigationBtn()
    }

    private fun onClickToolbarNavigationBtn() {
        binding.toolbarUserPaymentScreen.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


}

