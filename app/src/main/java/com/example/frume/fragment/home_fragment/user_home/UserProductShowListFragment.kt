package com.example.frume.fragment.home_fragment.user_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductShowListBinding
import com.example.frume.fragment.user_fragment.category.UserCategoryFragmentArgs


class UserProductShowListFragment : Fragment() {

    private var _binding: FragmentUserProductShowListBinding? = null
    private val binding get() = _binding!!

    // 변수명 : 어디서 보낸지(이전 카테고리 화면에서 보냈으니) bt navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_show_list, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickToolbar()

    }

    private fun onClickToolbar() {
        binding.toolbarUserProductShowList.setNavigationOnClickListener {
            findNavController().navigateUp() // 창을 버린다
        }
    }
}