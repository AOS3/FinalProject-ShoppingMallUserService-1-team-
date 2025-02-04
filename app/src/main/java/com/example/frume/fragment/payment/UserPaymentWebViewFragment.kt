package com.example.frume.fragment.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.frume.databinding.FragmentUserPaymentWebViewBinding

class UserPaymentWebViewFragment : Fragment() {
    private var _binding: FragmentUserPaymentWebViewBinding? = null
    private val binding get() = _binding!!
    private val args: UserPaymentWebViewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPaymentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showWebView()
    }

    private fun showWebView() {
        binding.webViewUserPaymentWebView.settings.javaScriptEnabled = true // 자바스크립트 활성화
        binding.webViewUserPaymentWebView.webViewClient = WebViewClient()
        val url = when (args.webViewType) {
            1 -> "https://sites.google.com/view/frumerefundexchange/%ED%99%88"
            else -> "https://sites.google.com/view/frumeshippingpayment/%ED%99%88"
        }
        binding.webViewUserPaymentWebView.loadUrl(url)
    }
}