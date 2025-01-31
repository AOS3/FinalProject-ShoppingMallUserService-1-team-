package com.example.frume.login

import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.frume.R


class AddressActivity : AppCompatActivity() {

    private lateinit var browser: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)  // layout_name 수정

        // WebView 설정
        browser = WebView(this)
        browser.settings.javaScriptEnabled = true
        browser.addJavascriptInterface(MyJavaScriptInterface(), "Android")

        // WebView가 페이지를 다 로드했을 때 호출되는 콜백
        browser.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                // 페이지 로딩이 끝났을 때 호출되는 메서드
                browser.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        })

        // WebView를 현재 액티비티의 레이아웃에 표시
        setContentView(browser)

        // 웹 페이지 로드
        browser.loadUrl("https://youngeunww2.blogspot.com/2025/01/api-test-window.html")
    }

    // JavaScript 인터페이스 클래스
    inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            // 자바스크립트로부터 주소 데이터를 받음
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    // 우편번호 데이터를 받아오는 메서드
    /*@JavascriptInterface
    fun processPostData(postData: String?) {
        // 자바스크립트로부터 우편번호 데이터를 받음
        val intent = Intent()
        intent.putExtra("postData", postData)
        setResult(RESULT_OK, intent)
        finish()
    }*/
}

