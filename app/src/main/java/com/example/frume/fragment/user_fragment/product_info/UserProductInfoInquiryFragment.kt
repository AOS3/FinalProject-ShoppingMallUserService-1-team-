package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.frume.data.Inquiry
import com.example.frume.data.Storage
import com.example.frume.databinding.FragmentUserProductInfoInquiryBinding
import com.example.frume.util.showToast


class UserProductInfoInquiryFragment : Fragment(),InquiryClickListener {
    private var _binding: FragmentUserProductInfoInquiryBinding? = null
    private val binding get() = _binding!!
    private var productDocId: String? = null

    private val adapter: ProductInquiryAdapter by lazy { ProductInquiryAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productDocId = it.getString(ARG_PRODUCT_DOC_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProductInfoInquiryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        onClickCheckBox()
    }

    private fun setLayout() {
        val inquiryList = Storage.inquiryList
        binding.rvProductInquiry.adapter = adapter
        adapter.updateList(inquiryList.toMutableList())
        binding.tvProductInquiry.text = "상품문의 (${inquiryList.size})"
    }

    private fun onClickCheckBox() {
        binding.cbProductInquiry.setOnCheckedChangeListener { _, isChecked ->
            val inquiryList = Storage.inquiryList
            val filteredList = if (isChecked) {
                inquiryList.filter { !it.secret }
            } else {
                inquiryList
            }
            binding.tvProductInquiry.text = "상품문의 (${filteredList.size})"
            adapter.updateList(filteredList.toMutableList())
        }
    }


    companion object {
        private const val ARG_PRODUCT_DOC_ID = "arg_number"
        fun newInstance(productDocId: String): UserProductInfoInquiryFragment {
            return UserProductInfoInquiryFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_DOC_ID, productDocId)
                }
            }
        }
    }

    override fun onClickInquiry(inquiry: Inquiry) {
        requireContext().showToast("추후 기능 개발 예정")
    }
}