package com.example.frume.fragment.home_fragment.my_info.order_inquiry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserOrderDetailBinding


// sehoon 주문 상세 내역
class UserOrderDetailFragment : Fragment() {
    private var _binding: FragmentUserOrderDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_order_detail, container, false)

        // 뒤로가기 버튼 리스너
        onClickNavigationIcon()

        // 반품/취소 화면 연결 버튼
        moveToCancelAndReturn()

        return binding.root
    }


    // 뒤로가기 버튼 리스너
    private fun onClickNavigationIcon() {

        binding.toolbarUserOrderDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 반품 교환 및 취소 화면 연결
    private fun moveToCancelAndReturn(){
        binding.buttonUserOrderDetailCancel.setOnClickListener{
            val action = UserOrderDetailFragmentDirections.actionUserOrderDetailToUserCancelAndReturn()
            findNavController().navigate(action)
        }
    }

}