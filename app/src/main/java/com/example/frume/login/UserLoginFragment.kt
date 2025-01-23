package com.example.frume.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.databinding.FragmentUserLoginBinding
import com.example.frume.home.HomeActivity
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
    // 로그인 처리 메서드
    fun proLogin() {
        binding.apply {
            // 입력 요소 검사
            if (binding.textFieldUserLoginId.editText?.text?.toString()?.isEmpty()!!) {
                /*  userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인") {
                      userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                  }
                  return*/
            }
            if (binding.textFieldUserLoginPw.editText?.text?.toString()?.isEmpty()!!) {
                /*    userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인") {
                        userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                    }
                    return*/

              /*  userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인") {
                    userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                }
                return*/

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
                // Log.d("test100", loginResult.str)
                // 로그인 결과로 분기한다.
                when (loginResult) {
                    LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {

                        /*     userActivity.showMessageDialog("로그인 실패", "존재하지 않는 아이디 입니다", "확인") {
                                 loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                                 loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                                 userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                             }*/
                    }

                    LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                        /* userActivity.showMessageDialog("로그인 실패", "잘못된 비밀번호 입니다", "확인") {
                             loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                             userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                         }*/
                    }

                    LoginResult.LOGIN_RESULT_SIGNOUT_MEMBER -> {
                        /*userActivity.showMessageDialog("로그인 실패", "탈퇴한 회원입니다", "확인") {

                   /*     userActivity.showMessageDialog("로그인 실패", "존재하지 않는 아이디 입니다", "확인") {

                            loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                        }*/
                    }



                    // hyeonseo 0123
                   LoginResult.LOGIN_RESULT_SUCCESS -> {
//                        // 로그인한 사용자 정보를 가져온다.
//                        val work2 = async(Dispatchers.IO) {
//                            UserService.selectUserDataByUserIdOne(loginUserId)
//                        }
//                        val loginUserModel = work2.await()
//
//                        // 만약 자동로그인이 체크되어 있다면
//                        if (binding.checkBoxUserLoginAuto) {
//                            CoroutineScope(Dispatchers.Main).launch {
//                                val work1 = async(Dispatchers.IO) {
//                                    UserService.updateUserAutoLoginToken(
//                                        userActivity,
//                                        loginUserModel.userDocumentId
//                                    )
//                                }
//                                work1.join()
//                            }
//                        }
//
//
//                        val intent = Intent(requireContext(), HomeActivity::class.java)
//                        intent.putExtra("user_document_id", loginUserModel.customerUserDocId)
//                        startActivity(intent)
//                        loginActivity.finish()
                  }

                }
            }
        }
    }

                    LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                       /* userActivity.showMessageDialog("로그인 실패", "잘못된 비밀번호 입니다", "확인") {
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                        }*/
                    }

                    LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER -> {
                        /*userActivity.showMessageDialog("로그인 실패", "탈퇴한 회원입니다", "확인") {
                            loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                        }*/
                    }

}

                    LoginResult.LOGIN_RESULT_SUCCESS -> {
                        // 로그인한 사용자 정보를 가져온다.
                        val work2 = async(Dispatchers.IO) {
                            UserService.selectUserDataByUserIdOne(loginUserId)
                        }
                        val loginUserModel = work2.await()
/*
                        // 만약 자동로그인이 체크되어 있다면
                        if (loginViewModel?.checkBoxUserLoginAutoChecked?.value!!) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val work1 = async(Dispatchers.IO) {
                                    UserService.updateUserAutoLoginToken(
                                        userActivity,
                                        loginUserModel.userDocumentId
                                    )
                                }
                                work1.join()
                            }
                        }*/

                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        intent.putExtra("user_document_id", loginUserModel.customerUserDocId)
                        startActivity(intent)
                        loginActivity.finish()
                    }

                }
            }
        }
    }
}

