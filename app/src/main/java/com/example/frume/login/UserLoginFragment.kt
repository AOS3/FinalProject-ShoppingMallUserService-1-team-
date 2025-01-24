package com.example.frume.login


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserLoginBinding
import com.example.frume.home.HomeActivity
import com.example.frume.model.UserModel
import com.example.frume.service.UserService
import com.example.frume.util.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch



class UserLoginFragment : Fragment() {
    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_login, container, false)
        loginActivity = activity as LoginActivity
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

        onClickLoginBtn()
        onClickSignUpBtn()
        onClickNonMemberLoginBtn()
        setupErrorResetListeners()

    }

    // sehoon 홈 화면 이동 메서드
    private fun moveToHomeScreen() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.putExtra("user_document_id", "noUser")
        startActivity(intent)
        loginActivity.finish()
    }

    // sehoon 로그인 버튼 클릭 메서드
    private fun onClickLoginBtn() {
        binding.buttonUserSignUp.setOnClickListener {
            proLogin()
        }
    }

    // sehoon 비회원 접속 버튼 클릭 메서드
    private fun onClickNonMemberLoginBtn() {
        binding.buttonUserLoginNonMember.setOnClickListener {
            moveToHomeScreen()
        }
    }

    // sehoon 회원가입 버튼 클릭 메서드
    private fun onClickSignUpBtn() {
        binding.textViewUserLoginSignUpButton.setOnClickListener {
            val action = UserLoginFragmentDirections.actionUserLoginToUserSignup()
            findNavController().navigate(action)
        }
    }


    // 에러 리셋 리스너 설정
    private fun setupErrorResetListeners() {
        binding.apply {
            // ID 입력 필드의 에러 리셋
            textFieldUserLoginId.editText?.let { editText ->
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        textFieldUserLoginId.error = null // 에러 초기화
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })
            }

            // 비밀번호 입력 필드의 에러 리셋
            textFieldUserLoginPw.editText?.let { editText ->
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        textFieldUserLoginPw.error = null // 에러 초기화
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }
    }

    // 로그인 처리 메서드
    fun proLogin() {
        binding.apply {
            // 입력 요소 검사
            if (binding.textFieldUserLoginId.editText?.text?.toString()?.isEmpty()!!) {
                binding.textFieldUserLoginId.error = "아이디를 입력해주세요"
                return
            }
            if (binding.textFieldUserLoginPw.editText?.text?.toString()?.isEmpty()!!) {

                binding.textFieldUserLoginPw.error = "비밀번호를 입력해주세요"
                return
            }

            // 사용자가 입력한 아이디와 비밀번호
            val loginUserId = binding.textFieldUserLoginId.editText?.text.toString()
            val loginUserPw = binding.textFieldUserLoginPw.editText?.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    UserService.checkLogin(loginUserId, loginUserPw)
                }
                // 로그인 결과를 가져온다.
                val loginResult = work1.await()


                // 로그인 결과로 분기한다.
                when (loginResult) {
                    LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {
                        binding.textFieldUserLoginId.error = "존재하지 않는 아이디 입니다"
                    }

                    LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                        binding.textFieldUserLoginPw.error = "잘못된 비밀번호입니다"
                    }

                    LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER -> {
                        binding.textFieldUserLoginId.error = "탈퇴한 회원입니다"
                        binding.textFieldUserLoginPw.error = "탈퇴한 회원입니다"
                    }

                    LoginResult.LOGIN_RESULT_SUCCESS -> {
                      
                        val work2 = async(Dispatchers.IO) {
                            UserService.selectUserDataByUserIdOne(loginUserId)
                        }
                        val loginUserModel = work2.await()

                        // 만약 자동로그인이 체크되어 있다면
                        if (binding.checkBoxUserLoginAuto.isChecked) {
                            CoroutineScope(Dispatchers.Main).launch {

                                val work1 = async(Dispatchers.IO) {
                                    UserService.updateUserAutoLoginToken(
                                        loginActivity,
                                        loginUserModel.customerUserDocId
                                    )
                                }
                                work1.join()
                            }
                        }
                        // HomeActivity를 실행하고 현재 Activity를 종료한다.
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        intent.putExtra("user_document_id", loginUserModel.customerUserDocId)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
    }
}
