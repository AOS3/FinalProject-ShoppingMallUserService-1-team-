package com.example.frume.home

import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.frume.R
import com.example.frume.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.concurrent.thread
import android.os.SystemClock
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    // 사용자 문서 iD
    var loginUserDocumentId = ""
    // 사용자 장바구니 문서 ID
    var userCartDocId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        // 사용자 문서 id와 닉네임을 받는다.
        loginUserDocumentId = intent.getStringExtra("user_document_id")!!
        userCartDocId = intent.getStringExtra("user_cart_document_id")!!
        setBottomNavigation()
    }

    // 바텀네비게이션 연결
    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_home) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav_home).setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home,
                R.id.navigation_category,
                R.id.navigation_cart,
                R.id.navigation_profile -> {
                    binding.bottomNavHome.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavHome.visibility = View.GONE
                }
            }
        }
    }

    // 키보드 올리는 메서드
    fun showSoftInput(view: View){
        // 입력을 관리하는 매니저
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // 포커스를 준다.
        view.requestFocus()

        thread {
            SystemClock.sleep(500)
            // 키보드를 올린다.
            inputManager.showSoftInput(view, 0)
        }
    }
    // 키보드를 내리는 메서드
    fun hideSoftInput(){
        // 포커스가 있는 뷰가 있다면
        if(currentFocus != null){
            // 입력을 관리하는 매니저
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            // 키보드를 내린다.
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스를 해제한다.
            currentFocus?.clearFocus()
        }
    }

    // Activity에서 터치가 발생하면 호출되는 메서드
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 만약 포커스가 주어진 View가 있다면
        if(currentFocus != null){
            // 현재 포커스가 주어진 View의 화면상의 영역 정보를 가져온다.
            val rect = Rect()
            currentFocus?.getGlobalVisibleRect(rect)
            // 현재 터치 지점이 포커스를 가지고 있는 View의 영역 내부가 아니라면
            if(rect.contains(ev?.x?.toInt()!!, ev?.y?.toInt()!!) == false){
                // 키보드를 내리고 포커스를 제거한다.
                hideSoftInput()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 다이얼로그를 통해 메시지를 보여주는 함수
    fun showConfirmationDialog(
        title: String,
        message: String,
        positiveTitle: String = "네",
        negativeTitle: String = "아니요",
        onPositive: () -> Unit,
        onNegative: () -> Unit = {}
    ) {
        val builder = MaterialAlertDialogBuilder(this@HomeActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveTitle) { dialogInterface: DialogInterface, i: Int ->
            onPositive()
        }
        builder.setNegativeButton(negativeTitle) { dialogInterface: DialogInterface, i: Int ->
            onNegative()
        }
        builder.show()
    }

}