package com.example.frume.fragment.user_fragment.user_payment

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
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

    lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_payment_screen, container, false)
        homeActivity = activity as HomeActivity


        // 툴바 뒤로가기
        onClickToolbarNavigationBtn()

        // 카드 선택 -> 카드사 선택 드롭 다운
        setupPaymentCardDropdown()

        setupPaymentMethodButtons()
        setupCheckBoxListeners()

        return binding.root

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

    // 카드 드롭다운 리스너
    private fun setupPaymentCardDropdown() {

        val autoCompletePaymentCardTextView = binding.autoCompleteTextViewUserPaymentCard

        // 드롭다운 데이터 정의
        val cardTypes = listOf("선택", "삼성", "신한", "국민", "롯데", "현대", "하나", "NH", "우리", "카카오뱅크", "비씨")

        // ArrayAdapter 생성 (autoCompletePaymentCardTextView에 데이터 연결)
        val adapterCardType = ArrayAdapter(
            homeActivity,
            android.R.layout.simple_dropdown_item_1line,
            cardTypes
        )

        // autoCompletePaymentCardTextView에 연결
        autoCompletePaymentCardTextView.setAdapter(adapterCardType)

        // autoCompletePaymentCardTextView 항목 선택 이벤트 리스너 설정
        autoCompletePaymentCardTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCardType = parent.getItemAtPosition(position).toString()
            // 선택된 항목 처리
            Toast.makeText(requireContext(), "선택된 상태 : $selectedCardType", Toast.LENGTH_SHORT).show()
        }
    }

    // 결제 방식 버튼을 단일 선택만 가능하도록 하는 메서드
    private fun setupPaymentMethodButtons() {

        // 버튼을 리스트로 묶어 관리
        val buttons = listOf(
            binding.buttonUserPaymentPaymentMethodAccount,
            binding.buttonUserPaymentPaymentMethodCard,
            binding.buttonUserPaymentPaymentMethodKakaoPay,
            binding.buttonUserPaymentPaymentMethodNaverPay
        )

        // 각 버튼에 대한 클릭 이벤트
        buttons.forEach { button ->
            button.setOnClickListener {
                // 모든 버튼을 비활성화 스타일(AppButtonCancel)로 변경
                buttons.forEach { btn ->
                    btn.setBackgroundResource(R.drawable.background_gray100) // 비활성화 배경
                    //btn.setTextAppearance(R.style.AppButtonCancel) // 비활성화 텍스트 스타일
                }

                // 클릭된 버튼을 활성화 스타일(CustomButtonStyle)로 변경
                button.setBackgroundResource(R.drawable.background_green100) // 활성화 배경
                //button.setTextAppearance(R.style.CustomButtonStyle) // 활성화 텍스트 스타일


                // 각 버튼에 따른 추가 동작 처리
                when (button.id) {
                    R.id.buttonUserPaymentPaymentMethodAccount -> {
                        // 계좌이체 버튼 선택 시 동작
                        //Toast.makeText(requireContext(), "계좌이체 선택", Toast.LENGTH_SHORT).show()
                        /*       collapseView(binding.textInputLayoutUserPaymentCard)
                               collapseView(binding.textViewUserPaymentCard)
                               collapseView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentStar.visibility = View.GONE
                        }
                    }

                    R.id.buttonUserPaymentPaymentMethodCard -> {
                        // 신용카드 버튼 선택 시 동작
                        //Toast.makeText(requireContext(), "신용카드 선택", Toast.LENGTH_SHORT).show()
                        /*expandView(binding.textInputLayoutUserPaymentCard)
                        expandView(binding.textViewUserPaymentCard)
                        expandView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.VISIBLE
                            textViewUserPaymentCard.visibility = View.VISIBLE
                            textViewUserPaymentStar.visibility = View.VISIBLE
                        }

                    }

                    R.id.buttonUserPaymentPaymentMethodKakaoPay -> {
                        // 카카오페이 버튼 선택 시 동작
                        //Toast.makeText(requireContext(), "카카오페이 선택", Toast.LENGTH_SHORT).show()
                        /*  collapseView(binding.textInputLayoutUserPaymentCard)
                          collapseView(binding.textViewUserPaymentCard)
                          collapseView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentStar.visibility = View.GONE
                        }
                    }

                    R.id.buttonUserPaymentPaymentMethodNaverPay -> {
                        // 네이버페이 버튼 선택 시 동작
                        //Toast.makeText(requireContext(), "네이버페이 선택", Toast.LENGTH_SHORT).show()
                        /*      collapseView(binding.textInputLayoutUserPaymentCard)
                              collapseView(binding.textViewUserPaymentCard)
                              collapseView(binding.textViewUserPaymentStar)*/
                        binding.apply {
                            textInputLayoutUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentCard.visibility = View.GONE
                            textViewUserPaymentStar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun expandView(view: View) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val targetHeight = view.measuredHeight

        view.layoutParams.height = 0
        view.visibility = View.VISIBLE

        val animation = ValueAnimator.ofInt(50, targetHeight)
        animation.addUpdateListener { animator ->
            view.layoutParams.height = animator.animatedValue as Int
            view.requestLayout()
        }

        animation.duration = 500
        animation.start()
    }

    private fun collapseView(view: View) {
        val initialHeight = view.height
        val animation = ValueAnimator.ofInt(initialHeight, 50)

        animation.addUpdateListener { animator ->
            view.layoutParams.height = animator.animatedValue as Int
            view.requestLayout()
        }

        animation.duration = 500 // 지속 시간 조정

        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animation.start()
    }


    private fun setupCheckBoxListeners() {
        val topCheckBox = binding.checkboxUserPaymentAgreeAll
        val otherCheckBoxes = listOf(
            binding.checkboxUserPaymentAgree1,
            binding.checkboxUserPaymentAgree2
        )
        val orderButton = binding.buttonUserCartOrder

        // 최상단 체크박스 상태 변경 시 다른 체크박스 모두 변경
        topCheckBox.setOnCheckedChangeListener { _, isChecked ->
            otherCheckBoxes.forEach { it.isChecked = isChecked }
        }

        // 하위 체크박스 상태 변경 시 최상단 체크박스 상태 업데이트
        otherCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                // 모든 하위 체크박스가 선택된 경우만 최상단 체크박스를 선택
                topCheckBox.isChecked = otherCheckBoxes.all { it.isChecked }
                updateOrderButtonState(orderButton, otherCheckBoxes)
            }
        }
    }

    // 결제 버튼 활성화
    private fun updateOrderButtonState(button: View, checkBoxes: List<View>) {
        // 모든 체크박스가 선택되었을 때만 버튼 활성화
        button.isEnabled = checkBoxes.all { (it as? androidx.appcompat.widget.AppCompatCheckBox)?.isChecked == true }
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