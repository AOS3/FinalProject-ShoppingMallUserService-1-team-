package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.databinding.FragmentUserProductInfoDetailBinding
import com.example.frume.databinding.FragmentUserProductInfoDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserProductInfoDialogFragment() : BottomSheetDialogFragment() {
    private var _binding: FragmentUserProductInfoDialogBinding? = null
    private val binding get() = _binding!!

    // 바텀시트 사이즈 조절
    override fun onStart() {
        super.onStart() // 부모 클래스의 onStart()를 호출하여 정상적인 라이프사이클 동작을 보장합니다.
        // 바텀시트 열릴때 최대크기로
        expandBottomSheetSize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_dialog, container, false)

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

        // Done 버튼 누를때 반응하는 리스너
        settingEditTextDoneAction()
    }


    // 바텀시트 펼치기
    private fun expandBottomSheetSize() {
        // Log.d("test200", "increase")
        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            // 화면의 전체 높이를 가져옵니다.
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels // 화면 높이를 픽셀 단위로 가져옵니다.

            // 바텀시트의 높이를 전체 화면 크기로 고정합니다.
            bottomSheet.layoutParams.height = screenHeight // 바텀시트의 레이아웃 높이를 설정합니다.
            bottomSheet.requestLayout() // 레이아웃 변경 사항을 적용합니다.

            // BottomSheetBehavior와 연결
            val behavior = BottomSheetBehavior.from(bottomSheet)
            // BottomSheetBehavior 상태를 확장으로 설정
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    // 바텀시트에서 homeActivity의 hideKeyboard메서드가 안먹어 직접 만들어 실행
    fun hideKeyboardAndClearFocus() {
        binding.apply {
            // 키보드 숨기기
//            val inputMethodManager = homeActivity.getSystemService(InputMethodManager::class.java)
//            inputMethodManager?.hideSoftInputFromWindow(
//                editTextProductInfoDialogCount.windowToken, 0
//            )
            // 포커스 해제
            editTextProductInfoDialogCount.clearFocus()
        }
    }

    // 키보드 확인 버튼
    private fun settingEditTextDoneAction() {
        Log.d("test200", "settingEditTextDoneAction")
        val editText = binding.editTextProductInfoDialogCount

        editText.setOnEditorActionListener { v, actionId, event ->
            // 'Done' 버튼이 눌렸을 때 actionId가 IME_ACTION_DONE인지를 확인
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리고 포커스 초기화 (홈액티비티에 있는 hideKeyboard 메서드가 작동안돼 직접 만듦
                hideKeyboardAndClearFocus()
                Log.d("test200", "done : $actionId")

                true  // 리스너가 이벤트를 처리했음을 반환
            } else {
                Log.d("test200", actionId.toString())
            }
            false
        }
    }
}

