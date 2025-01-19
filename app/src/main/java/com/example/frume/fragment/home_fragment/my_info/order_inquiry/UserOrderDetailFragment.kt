package com.example.frume.fragment.home_fragment.my_info.order_inquiry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.frume.R
import com.example.frume.databinding.FragmentUserOrderDetailBinding


// sehoon 주문 상세 내역
class UserOrderDetailFragment() : Fragment() {
    private var _binding: FragmentUserOrderDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_order_detail, container, false)
        return binding.root
    }

}