package com.example.frume.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.navigation.findNavController
import com.example.frume.R
import com.example.frume.databinding.ActivityLoginBinding
import com.example.frume.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen()
        //enableEdgeToEdge()

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragmentContainerViewUser)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // userAutoLoginProcessing()
        //
    }

    fun userAutoLoginProcessing() {
        val pref = getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
        val loginToken = pref.getString("token", null)

        CoroutineScope(Dispatchers.Main).launch {
            if (loginToken != null) {
                val work1 = async(Dispatchers.IO) {
                    UserService.selectUserDataByLoginToken(loginToken)
                }
                val userVO = work1.await()

                if (userVO != null) {
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("user_document_id", userVO.customerUserDocId)
                    startActivity(intent)
                    finish()
                } else {
                    findNavController(R.id.fragmentContainerViewUser)
                        .navigate(R.id.user_login)
                }
            } else {
                findNavController(R.id.fragmentContainerViewUser)
                    .navigate(R.id.user_login)
            }
        }
    }
}

