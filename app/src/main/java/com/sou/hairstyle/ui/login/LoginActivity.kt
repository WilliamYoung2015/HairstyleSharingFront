package com.sou.hairstyle.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sou.hairstyle.MainActivity
import com.sou.hairstyle.R
import com.sou.hairstyle.common.PublicData
import com.sou.hairstyle.common.SharedPreferencesManager
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.ui.register.RegisterActivity
import com.sou.hairstyle.ui.resetpassword.ResetPasswordActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val phoneNumberEditText: EditText = findViewById(R.id.editTextPhone)
        val passwordEditText: EditText = findViewById(R.id.password_input)
        val loginButton: Button = findViewById(R.id.button_login)
        val forgotPasswordTextView: TextView = findViewById(R.id.text_view_forgot_password_login)
        val createAccountTextView: TextView = findViewById(R.id.text_view_create_account)

        loginButton.setOnClickListener {
            viewModel.login(
                phone = phoneNumberEditText.text.toString().trim(),
                password = passwordEditText.text.toString().trim()
            )
        }

        viewModel.loginStatus.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator
                }
                is Resource.Success -> {
                    // Handle successful login
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    val sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
                    resource.data?.data?.user?.let {
                        sharedPreferencesManager.saveUser(it)
                        PublicData.loginUid = it.id
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    // Navigate to next screen
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        forgotPasswordTextView.setOnClickListener {
            // Handle forgot password click
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        createAccountTextView.setOnClickListener {
            // Handle create account click
            startActivity(Intent(this, RegisterActivity::class.java))
            finish() // 关闭当前活动
        }
    }
}
