package com.example.frume.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frume.R
import com.example.frume.databinding.ActivityLoginBinding
import com.example.frume.databinding.ActivityMainBinding
import com.example.frume.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        //enableEdgeToEdge()

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // 자동 로그인 검사 시작
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        // 비동기로 검사
        // Preference에 login token이 있는지 확인한다.
        val sharedPreferences = getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
        val loginToken = sharedPreferences.getString("token", null)

        CoroutineScope(Dispatchers.Main).launch {
            if (loginToken != null) {
                // 사용자 정보를 가져온다.
                val work1 = async(Dispatchers.IO) {
                    UserService.selectUserDataByLoginToken(loginToken)
                }
                val userVO = work1.await()

                // 사용자 데이터를 성공적으로 가져온 경우
                if (userVO != null) {
                    Log.d("AutoLogin", "자동 로그인 성공 - 사용자 정보: ${userVO.customerUserName}")

                    // HomeActivity로 이동
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    intent.putExtra("user_document_id", userVO.customerUserDocId)
                    startActivity(intent)
                    finish()  // MainActivity 종료
                } else {
                    Log.d("AutoLogin", "자동 로그인 실패 - 유효하지 않은 사용자 데이터")
                    navigateToLogin()  // 로그인 화면으로 이동
                }
            } else {
                Log.d("AutoLogin", "로그인 토큰이 없습니다.")
                navigateToLogin()  // 로그인 화면으로 이동
            }
        }
    }

    // LoginActivity로 이동하는 메서드
    private fun navigateToLogin() {
        Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // MainActivity 종료
    }
}