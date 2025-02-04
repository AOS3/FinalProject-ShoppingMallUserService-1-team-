package com.example.frume.util

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.frume.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

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

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showDialog(
    title: String, msg: String, pos: String, nega: String,
    onResult: (Boolean) -> Unit

) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
        .setMessage(msg)
        .setPositiveButton(pos) { dialog, _ ->
            dialog.dismiss()
            onResult(true)
        }
        .setNegativeButton(nega) { dialog, _ ->
            dialog.dismiss()
            onResult(false)
        }
    val alertDialog = builder.create()
    alertDialog.show()
}


@BindingAdapter("imageUri")
fun ImageView.load(uri: String?) {
    if (!uri.isNullOrEmpty()) {
        Glide.with(this)
            .load(Uri.parse(uri))
            .placeholder(R.color.gray10)
            .into(this)
    }
}


@BindingAdapter("formattedDate")
fun setFormattedDate(textView: TextView, timestamp: Timestamp?) {
    timestamp?.let {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        textView.text = sdf.format(it.toDate())
    } ?: run {
        textView.text = "N/A" // 값이 없을 경우 기본값
    }
}

@BindingAdapter("app:visibilityBasedOnText")
fun setVisibilityBasedOnText(view: View, text: String?) {
    view.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
}

fun Number.convertDigitCommaToString(): String {
    val decimalFormat = DecimalFormat("#") // 천 단위 구분자 없이 포맷
    return decimalFormat.format(this)
}

fun TextInputLayout.validatePhoneLength(errorMessage: String): Boolean {
    val inputText = this.editText?.text.toString().trim()
    return if (inputText.length!=11) {
        this.isErrorEnabled = true
        this.error = errorMessage
        false
    } else {
        this.isErrorEnabled = false
        this.error = null
        true
    }
}