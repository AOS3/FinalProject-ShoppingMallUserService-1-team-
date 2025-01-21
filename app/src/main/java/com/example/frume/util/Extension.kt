package com.example.frume.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat

fun TextInputLayout.validateInput(errorMessage: String): Boolean {
    val inputText = this.editText?.text.toString().trim()
    return if (inputText.isEmpty()) {
        this.isErrorEnabled = true
        this.error = errorMessage
        false
    } else {
        this.isErrorEnabled = false
        this.error = null
        true
    }
}

fun TextView.applyNumberFormat(amount: Int) {
    text = amount.convertThreeDigitComma()
}

fun Number.convertThreeDigitComma(): String {
    val decimalFormat = DecimalFormat("#,###원")
    return decimalFormat.format(this)
}