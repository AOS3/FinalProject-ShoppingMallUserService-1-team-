package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoWriteReviewBinding


class UserProductInfoWriteReviewFragment : Fragment() {
    private var _binding: FragmentUserProductInfoWriteReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_write_review, container, false)


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

    private fun setLayout() {
        onClickButtonReviewSubmit()
        onClickNavigationIconBackStack()
    }

    // sehoon 상품설명 화면 이동
    private fun moveToProductInfo() {
        findNavController().navigateUp()
    }

    // sehoon 후기 저장 버튼 클릭 메서드
    private fun onClickButtonReviewSubmit() {
        binding.buttonUserProductInfoReviewConfirm.setOnClickListener {
            moveToProductInfo()
        }
    }


    // sehoon 툴바 네비게이션 버튼 클릭 메서드
    private fun onClickNavigationIconBackStack() {
        binding.toolbarUserProductInfoWriteReview.apply {
            setNavigationOnClickListener {
                moveToProductInfo()
            }
        }
    }
}