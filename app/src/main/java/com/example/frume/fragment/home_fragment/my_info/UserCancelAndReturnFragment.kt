package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCancelAndReturnBinding

class UserCancelAndReturnFragment() : Fragment() {
    private var _binding: FragmentUserCancelAndReturnBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_cancel_and_return, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        onClickConfirmBtn()
    }

    private fun onClickConfirmBtn() {
        binding.buttonUserCancelAndReturnRequest.setOnClickListener {
            val action = UserCancelAndReturnFragmentDirections.actionUserCancelAndReturnFragmentToUserOrderDetail()
            findNavController().navigate(action)
        }
    }
}