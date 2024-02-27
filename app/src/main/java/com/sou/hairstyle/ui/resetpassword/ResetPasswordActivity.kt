package com.sou.hairstyle.ui.resetpassword

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sou.hairstyle.MainActivity
import com.sou.hairstyle.R
import com.sou.hairstyle.common.SharedPreferencesManager
import com.sou.hairstyle.network.Resource

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var phoneNumberEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var verificationCodeEditText: EditText
    private lateinit var sendVerificationCodeButton: Button
    private lateinit var resetPasswordButton: Button
    private lateinit var countdownTimerView: TextView

    private val viewModel: ResetPasswordViewModel by viewModels()
    private var smsNo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password) // Replace with your layout file name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(android.R.drawable.stat_sys_phone_call) // 示例系统图标

        phoneNumberEditText = findViewById(R.id.editTextPhone)
        passwordEditText = findViewById(R.id.password_input)
        verificationCodeEditText = findViewById(R.id.editTextCode)
        sendVerificationCodeButton = findViewById(R.id.buttonSendCode)
        resetPasswordButton = findViewById(R.id.button_reset_password)
        countdownTimerView = findViewById(R.id.textViewCountdown) // 初始化您的倒计时TextView
        countdownTimerView.visibility = View.GONE // 初始设置为不可见

        sendVerificationCodeButton.setOnClickListener {
            viewModel.sendVerificationCode(phoneNumberEditText.text.toString())
        }

        resetPasswordButton.setOnClickListener {
            // 检查smsNo是否被初始化
            if (smsNo != null) {
                viewModel.resetPassowrd(
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
                    smsNo = resource.data?.data?.sms_no.toString();
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


        // 观察重置密码的状态
        viewModel.resetPasswordStatus.observe(this, Observer { resource ->
            println("registrationStatus observer")
            println(resource)

            when (resource) {
                is Resource.Success -> {
                    // 处理重置密码成功的情况
                    Toast.makeText(this, "重置密码成功", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    // 处理重置密码失败的情况
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    // 显示加载状态
                }
            }
        })

        setupCountdownTimer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 当点击返回按钮时，结束当前活动
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
}
