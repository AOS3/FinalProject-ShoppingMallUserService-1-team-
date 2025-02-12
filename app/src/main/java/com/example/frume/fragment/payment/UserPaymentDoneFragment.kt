package com.example.frume.fragment.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.databinding.FragmentUserPaymentDoneBinding
import com.example.frume.util.convertThreeDigitComma


class UserPaymentDoneFragment : Fragment() {
    private var _binding: FragmentUserPaymentDoneBinding? = null
    private val binding get() = _binding!!
    private val args: UserPaymentDoneFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPaymentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        onClickBtn()
    }

    private fun setLayout() {
        with(binding) {
            tvPrice.text = args.paymentDone.totalPrice.convertThreeDigitComma()
            tvDate.text = args.paymentDone.paymentDate
            tvPayment.text = args.paymentDone.payment
        }
    }

    private fun onClickBtn() {
        binding.btnDone.setOnClickListener {
            val action = UserPaymentDoneFragmentDirections.actionUserPaymentDoneToNavigationHome()
            findNavController().navigate(action)
        }
    }
}