package com.example.frume.fragment.user_fragment.user_payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserPaymentScreenBinding
import com.example.frume.fragment.user_fragment.user_cart.UserCartFragmentDirections

class UserPaymentScreenFragment : Fragment() {

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
        onClickToolbarNavigationBtn()  // 툴바 내비게이션 버튼 클릭 리스너 설정
        onClickPaymentDeliverySpotChange()  // 배송지 변경 버튼 클릭 리스너 설정
        setupDeliveryWayRadioButtons()  // 배송 방식 라디오 버튼 설정
    }

    // 툴바 내비게이션 버튼 클릭 리스너
    private fun onClickToolbarNavigationBtn() {
        binding.toolbarUserPaymentScreen.setNavigationOnClickListener {
            // 내비게이션 아이콘을 클릭하면 이전 화면으로 돌아감
            findNavController().navigateUp()
        }
    }

    // 배송지 변경 아이콘 클릭 시 UserAddressManageFragment로 이동하는 메서드
    private fun onClickPaymentDeliverySpotChange() {
        binding.imageViewUserPaymentAddressModify.setOnClickListener {
            // 배송지 수정 버튼 클릭 시, 배송지 관리 화면으로 이동
            val action = UserPaymentScreenFragmentDirections.actionUserPaymentScreenToUserAddressManage()
            findNavController().navigate(action)
        }
    }

    // 라디오 버튼 설정 및 선택 처리
    private fun setupDeliveryWayRadioButtons() {
        // 기본적으로 '문 앞 배송'이 선택된 상태로 설정
        binding.buttonUserPaymentDeliveryMethodDoor.setBackgroundResource(R.drawable.background_green100)

        // 각 라디오 버튼에 클릭 리스너를 설정하여 버튼 선택 상태를 처리
        binding.buttonUserPaymentDeliveryMethodDoor.setOnClickListener {
            // '문 앞 배송' 버튼 클릭 시 선택된 상태로 처리
            toggleButtonSelection(binding.buttonUserPaymentDeliveryMethodDoor)
        }
        binding.buttonUserPaymentDeliveryMethodBox.setOnClickListener {
            // '택배함' 버튼 클릭 시 선택된 상태로 처리
            toggleButtonSelection(binding.buttonUserPaymentDeliveryMethodBox)
        }
        binding.buttonUserPaymentDeliveryMethodOffice.setOnClickListener {
            // '경비실' 버튼 클릭 시 선택된 상태로 처리
            toggleButtonSelection(binding.buttonUserPaymentDeliveryMethodOffice)
        }
    }

    // 선택된 버튼의 배경을 변경하고 나머지 버튼들은 기본 배경으로 설정
    private fun toggleButtonSelection(selectedButton: RadioButton) {
        // 모든 버튼을 기본 배경으로 리셋
        listOf(
            binding.buttonUserPaymentDeliveryMethodDoor,
            binding.buttonUserPaymentDeliveryMethodBox,
            binding.buttonUserPaymentDeliveryMethodOffice
        ).forEach { button ->
            // 모든 버튼에 대해 기본 배경을 설정 (회색 배경)
            button.setBackgroundResource(R.drawable.background_gray100)
        }

        // 선택된 버튼에만 초록색 배경을 적용
        selectedButton.setBackgroundResource(R.drawable.background_green100)
    }
}





