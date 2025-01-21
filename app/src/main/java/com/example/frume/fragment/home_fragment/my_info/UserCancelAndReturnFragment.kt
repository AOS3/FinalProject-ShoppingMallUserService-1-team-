package com.example.frume.fragment.home_fragment.my_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserCancelAndReturnBinding
import com.example.frume.home.HomeActivity

class UserCancelAndReturnFragment() : Fragment() {
    private var _binding: FragmentUserCancelAndReturnBinding? = null
    private val binding get() = _binding!!
    lateinit var homeActivity: HomeActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_cancel_and_return, container, false)

        homeActivity = activity as HomeActivity

        onClickNavigationIcon()

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
        setupCancelAndReturnMethodbuttons()
        setupCancelAndReturnDropdown()
    }

    private fun onClickConfirmBtn() {
        binding.buttonUserCancelAndReturnRequest.setOnClickListener {
            // 입력된 값 가져오기
            val reason = binding.autoCompleteTextViewUserCancelAndReturnReason.text.toString()
            val seletedType = binding.autoCompleteTextViewUserCancelAndReturn.text.toString()
            val validTypes = listOf("반품", "취소", "교환") // 드롭다운에 있는 유효한 값 목록

            // 입력 값 검증: 선택되지 않았거나 유효하지 않은 경우
            if (!validTypes.contains(seletedType) ) {
                Toast.makeText(requireContext(), "반품/교환/취소 중 하나를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 작업 중단
            }

            // 반품/교환/취소 사유 입력 값이 비어있다면 경고 메시지 출력
            if (reason.isBlank()) {
                Toast.makeText(requireContext(), "반품/교환/취소 사유를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 작업 중단
            }

            // 입력 값이 유효한 경우 동작 수행
            Toast.makeText(requireContext(), "반품, 취소 및 교환 신청 완료", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    // 뒤로가기 버튼 리스너
    private fun onClickNavigationIcon() {

        binding.toolbarUserCancelAndReturn.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupCancelAndReturnMethodbuttons() {

        // 버튼을 리스트로 묶어 관리
        val buttons = listOf(
            binding.buttonUserCancelAndReturnDirection,
            binding.buttonUserCancelAndReturnPost
        )

        // 각 버튼에 대한 클릭 이벤트
        buttons.forEach { button ->
            button.setOnClickListener {
                // 모든 버튼을 비활성화 스타일(AppButtonCancel)로 변경
                buttons.forEach { btn ->
                    btn.setBackgroundResource(R.drawable.background_gray100) // 비활성화 배경
                }

                // 클릭된 버튼을 활성화 스타일(CustomButtonStyle)로 변경
                button.setBackgroundResource(R.drawable.background_green100) // 활성화 배경
            }
        }
    }

    // 카드 드롭다운 리스너
    private fun setupCancelAndReturnDropdown(){

        val autoCompleteTextViewUserCancelAndReturn = binding.autoCompleteTextViewUserCancelAndReturn

        // 드롭다운 데이터 정의
        val CancelAndReturnTypes = listOf("반품", "취소", "교환")

        // ArrayAdapter 생성 (autoCompleteTextViewUserCancelAndReturn에 데이터 연결)
        val adapterCancelAndReturnType = ArrayAdapter(
            homeActivity,
            android.R.layout.simple_dropdown_item_1line,
            CancelAndReturnTypes
        )

        // autoCompletePaymentCardTextView에 연결
        autoCompleteTextViewUserCancelAndReturn.setAdapter(adapterCancelAndReturnType)

    }


}
