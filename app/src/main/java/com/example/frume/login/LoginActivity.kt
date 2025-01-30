package com.example.frume.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.frume.R
import com.example.frume.databinding.ActivityLoginBinding
import com.example.frume.home.HomeActivity
import com.example.frume.service.UserService
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    lateinit var activityLoginBinding: ActivityLoginBinding

    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // userAutoLoginProcessing()
    }


    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: UserFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){
            // 로그인 화면
            UserFragmentName.USER_LOGIN_FRAGMENT -> UserLoginFragment()
            // 회원 가입 과정1 화면
            UserFragmentName.USER_SIGN_UP_FRAGMENT -> UserSignUpFragment()
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment?.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

            if(animate) {
                // 만약 이전 프래그먼트가 있다면
                if(oldFragment != null){
                    oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                    oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                }

                newFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerViewUser, newFragment!!)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }


    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: UserFragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    // hyeonseo 0123
    // 자동 로그인 처리 메서드
    fun userAutoLoginProcessing(){

        // Preference에 login token이 있는지 확인한다.
        val pref = getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
        val loginToken = pref.getString("token", null)

        CoroutineScope(Dispatchers.Main).launch {
            if(loginToken != null){
                // 사용자 정보를 가져온다.
                val work1 = async(Dispatchers.IO){
                    UserService.selectUserDataByLoginToken(loginToken)
                }
                val userVO = work1.await()

                // 가져온 사용자 데이터가 있다면
                if(userVO != null){
                    // BoardActivity를 실행하고 현재 Activity를 종료한다.
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("user_document_id", userVO.customerUserDocId)
                    startActivity(intent)
                    finish()
                } else {
                    // 첫번째 Fragment를 설정한다.
                    replaceFragment(UserFragmentName.USER_LOGIN_FRAGMENT,false,false,null)
                }
            } else {
                // 첫번째 Fragment를 설정한다.
                replaceFragment(UserFragmentName.USER_LOGIN_FRAGMENT, false, false, null)
            }
        }
    }
}

// 프래그먼트들을 나타내는 값들
enum class UserFragmentName(var number:Int, var str:String){
    // 로그인 화면
    USER_LOGIN_FRAGMENT(1, "UserLoginFragment"),
    // 회원 가입 화면
    USER_SIGN_UP_FRAGMENT(2, "UserSignUpFragment"),
}