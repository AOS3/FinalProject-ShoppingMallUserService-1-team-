package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoDetailBinding


class UserProductInfoDetailFragment: Fragment() {
    private var _binding: FragmentUserProductInfoDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_detail, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setLayout() {

    }

    companion object {
        private const val ARG_NUMBER = "arg_number"
        fun newInstance(): UserProductInfoDetailFragment {
            return UserProductInfoDetailFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    // putInt(ARG_NUMBER, number)

                }
            }
        }
    }

}