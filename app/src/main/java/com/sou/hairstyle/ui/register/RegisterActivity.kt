package com.sou.hairstyle.ui.register

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sou.hairstyle.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var phoneNumberEditText: EditText
    private lateinit var verificationCodeEditText: EditText
    private lateinit var sendVerificationCodeButton: Button
    private lateinit var registerButton: Button
    private lateinit var existingAccountTextView: TextView
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var countdownTimerView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Replace with your layout file name

        phoneNumberEditText = findViewById(R.id.editTextPhone)
        verificationCodeEditText = findViewById(R.id.editTextCode)
        sendVerificationCodeButton = findViewById(R.id.buttonSendCode)
        registerButton = findViewById(R.id.buttonRegister)
        existingAccountTextView = findViewById(R.id.textViewExistingAccount)
        forgotPasswordTextView = findViewById(R.id.textViewForgotPassword)
        countdownTimerView = findViewById(R.id.textViewCountdown) // 初始化您的倒计时TextView
        countdownTimerView.visibility = View.GONE // 初始设置为不可见

        sendVerificationCodeButton.setOnClickListener {
            sendVerificationCode()
        }

        registerButton.setOnClickListener {
            register()
        }

        existingAccountTextView.setOnClickListener {
            navigateToLogin()
        }

        forgotPasswordTextView.setOnClickListener {
            navigateToResetPassword()
        }
    }

    private fun sendVerificationCode() {
        val phoneNumber = phoneNumberEditText.text.toString()
        if (phoneNumber.isNotEmpty()) {
            // Start countdown timer for the verification button
            object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    sendVerificationCodeButton.visibility = View.GONE // 隐藏发送按钮
                    countdownTimerView.visibility = View.VISIBLE // 显示倒计时
                    countdownTimerView.text = "重新发送 (${millisUntilFinished / 1000}s)"
                }

                override fun onFinish() {
                    countdownTimerView.visibility = View.GONE // 隐藏倒计时
                    sendVerificationCodeButton.visibility = View.VISIBLE // 显示发送按钮
                    sendVerificationCodeButton.isEnabled = true
                    sendVerificationCodeButton.text = "发送验证码"
                }
            }.start()
        } else {
            // Show error or request focus for the phone number input
            phoneNumberEditText.error = "请输入手机号码"
            phoneNumberEditText.requestFocus()
        }
    }

    private fun register() {
        // TODO: Add logic for registration with the verification code
        val phoneNumber = phoneNumberEditText.text.toString()
        val verificationCode = verificationCodeEditText.text.toString()
        if (phoneNumber.isNotEmpty() && verificationCode.isNotEmpty()) {
            // Logic to verify the code and register the user
        } else {
            // Handle the case where either or both fields are empty
            if (phoneNumber.isEmpty()) {
                phoneNumberEditText.error = "手机号码不能为空"
            }
            if (verificationCode.isEmpty()) {
                verificationCodeEditText.error = "验证码不能为空"
            }
        }
    }

    private fun navigateToLogin() {
        // TODO: Navigate to the login activity
    }

    private fun navigateToResetPassword() {
        // TODO: Navigate to the reset password activity
    }
}
