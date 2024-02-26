package com.sou.hairstyle.ui.register

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sou.hairstyle.R
import com.sou.hairstyle.network.Resource

class RegisterActivity : AppCompatActivity() {
    private lateinit var phoneNumberEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var verificationCodeEditText: EditText
    private lateinit var sendVerificationCodeButton: Button
    private lateinit var registerButton: Button
    private lateinit var existingAccountTextView: TextView
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var countdownTimerView: TextView

    private val viewModel: RegisterViewModel by viewModels()
    private var smsNo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Replace with your layout file name

        phoneNumberEditText = findViewById(R.id.editTextPhone)
        passwordEditText = findViewById(R.id.password_input)
        verificationCodeEditText = findViewById(R.id.editTextCode)
        sendVerificationCodeButton = findViewById(R.id.buttonSendCode)
        registerButton = findViewById(R.id.buttonRegister)
        existingAccountTextView = findViewById(R.id.textViewExistingAccount)
        forgotPasswordTextView = findViewById(R.id.textViewForgotPassword)
        countdownTimerView = findViewById(R.id.textViewCountdown) // 初始化您的倒计时TextView
        countdownTimerView.visibility = View.GONE // 初始设置为不可见

        sendVerificationCodeButton.setOnClickListener {
            viewModel.sendVerificationCode(phoneNumberEditText.text.toString())
        }

        registerButton.setOnClickListener {
            // 检查smsNo是否被初始化
            smsNo = "11111111"
            if (smsNo != null) {
                viewModel.register(
                    phoneNumberEditText.text.toString(),
                    smsNo!!,
                    verificationCodeEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            } else {
                // 如果smsNo未被初始化，显示错误消息
                Toast.makeText(this, "请先获取验证码。", Toast.LENGTH_SHORT).show()
            }
        }

        // 观察验证码发送状态
        viewModel.smsStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    sendVerificationCodeButton.isEnabled = false
                    countdownTimerView.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    sendVerificationCodeButton.visibility = View.VISIBLE
                    countdownTimerView.visibility = View.GONE
                    sendVerificationCodeButton.isEnabled = true
                    smsNo = resource.data?.sms_no.toString();
                    // 可以选择显示某些成功信息
                }
                is Resource.Error -> {
                    sendVerificationCodeButton.visibility = View.VISIBLE
                    countdownTimerView.visibility = View.GONE
                    sendVerificationCodeButton.isEnabled = true
                    // 显示错误信息
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }


        // 观察注册状态
        viewModel.registrationStatus.observe(this, Observer { resource ->
            println("registrationStatus observer")
            println(resource)

            when (resource) {
                is Resource.Success -> {
                    // 处理注册成功的情况

                }
                is Resource.Error -> {
                    // 处理注册失败的情况
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    // 显示加载状态
                }
            }
        })

        existingAccountTextView.setOnClickListener {
            navigateToLogin()
        }

        forgotPasswordTextView.setOnClickListener {
            navigateToResetPassword()
        }

        setupCountdownTimer()
    }

    private fun setupCountdownTimer() {
        viewModel.countdownTime.observe(this) { secondsRemaining ->
            if (secondsRemaining > 0) {
                countdownTimerView.visibility = View.VISIBLE
                countdownTimerView.text = getString(R.string.resend_code_timer, secondsRemaining)
                sendVerificationCodeButton.isEnabled = false
            } else {
                countdownTimerView.visibility = View.GONE
                sendVerificationCodeButton.visibility = View.VISIBLE
                sendVerificationCodeButton.isEnabled = true
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
