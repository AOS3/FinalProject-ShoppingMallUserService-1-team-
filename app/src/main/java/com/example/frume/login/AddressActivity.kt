package com.example.frume.login

import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
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
}



/*import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.frume.R

class AddressActivity : AppCompatActivity() {

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        webView = findViewById(R.id.webView)

        webView!!.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            webViewClient = client
            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                    val newWebView = WebView(this@AddressActivity)
                    newWebView.settings.javaScriptEnabled = true
                    val dialog = Dialog(this@AddressActivity).apply {
                        setContentView(newWebView)
                    }
                    dialog.show()
                    val lp = WindowManager.LayoutParams().apply {
                        copyFrom(dialog.window!!.attributes)
                        width = WindowManager.LayoutParams.MATCH_PARENT
                        height = WindowManager.LayoutParams.MATCH_PARENT
                    }
                    dialog.window!!.attributes = lp
                    newWebView.webChromeClient = object : WebChromeClient() {
                        override fun onCloseWindow(window: WebView) {
                            dialog.dismiss()
                        }
                    }
                    (resultMsg.obj as WebView.WebViewTransport).webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }
            }
        }

        webView!!.loadUrl("https://youngeunww2.blogspot.com/2025/01/api-test-window.html")
    }

    private val client: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }
    }
}*/

