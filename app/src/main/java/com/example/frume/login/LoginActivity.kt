package com.example.frume.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frume.R
import com.example.frume.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // hyeonseo 0123
//    // 자동 로그인 처리 메서드
//    // 자동 로그인에 실패하면 LoginFragment를 띄우고
//    // 자동 로그인에 성공하면 현재 Activity를 종료하고 BoardActivity를 실행킨다.
//    fun userAutoLoginProcessing(){
//        // Preference에 login token이 있는지 확인한다.
//        val pref = getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
//        val loginToken = pref.getString("token", null)
//        // Log.d("test100", "$loginToken")
//
//        CoroutineScope(Dispatchers.Main).launch {
//            if(loginToken != null){
//                // 사용자 정보를 가져온다.
//                val work1 = async(Dispatchers.IO){
//                    UserService.selectUserDataByLoginToken(loginToken)
//                }
//                val loginUserModel = work1.await()
//                // 가져온 사용자 데이터가 있다면
//                if(loginUserModel != null){
//                    // BoardActivity를 실행하고 현재 Activity를 종료한다.
//                    val boardIntent = Intent(this@UserActivity, BoardActivity::class.java)
//                    boardIntent.putExtra("user_document_id", loginUserModel.userDocumentId)
//                    boardIntent.putExtra("user_nick_name", loginUserModel.userNickName)
//                    startActivity(boardIntent)
//                    finish()
//                } else {
//
//                }
//            } else {
//
//            }
//        }
//    }
}