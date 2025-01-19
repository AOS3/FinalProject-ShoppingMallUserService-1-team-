package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoInquiryBinding


class UserProductInfoInquiryFragment : Fragment() {
    private var _binding: FragmentUserProductInfoInquiryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_inquiry, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    companion object {

        fun newInstance(): UserProductInfoInquiryFragment {
            return UserProductInfoInquiryFragment().apply {
                // 값 전달 코드 번들 사용

            }
        }
    }
}