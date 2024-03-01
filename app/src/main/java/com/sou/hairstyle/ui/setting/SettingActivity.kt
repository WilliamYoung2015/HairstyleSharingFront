package com.sou.hairstyle.ui.setting

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sou.hairstyle.R
import com.sou.hairstyle.databinding.ActivitySettingBinding
import com.sou.hairstyle.network.Request.UpdateUserRequest
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.network.Response.User

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
//    private val viewModel: SettingViewModel by viewModels()
    private lateinit var viewModel: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setTitle(R.string.settings);
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.userInit.observe(this) { user ->
            binding.nicknameInput.setText(user?.nickname)
            binding.editAgeNumber.setText(user?.age.toString())
            binding.genderInput.setText(user?.gender)
            binding.addressInput.setText(user?.address)
            binding.styleInput.setText(user?.my_style)
        }

        binding.saveButton.setOnClickListener {
           saveSettings()
        }

        binding.buttonDecrement.setOnClickListener {
            // Decrement age logic
            val currentAge = binding.editAgeNumber.text.toString().toIntOrNull() ?: 0
            if (currentAge > 0) { // Assuming the age cannot be less than 0
                val newAge = currentAge - 1
                binding.editAgeNumber.setText(newAge.toString())
            }
        }

        binding.buttonIncrement.setOnClickListener {
            // Increment age logic
            val currentAge = binding.editAgeNumber.text.toString().toIntOrNull() ?: 0
            val newAge = currentAge + 1
            binding.editAgeNumber.setText(newAge.toString())
        }


        viewModel.userSettings.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Toast.makeText(this, "个人信息更新成功", Toast.LENGTH_SHORT).show()
                    finish()
                    // 可以选择显示某些成功信息
                }
                is Resource.Error -> {

                    // 显示错误信息
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }
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

    // Inside SettingActivity class, in the saveSettings() function
    private fun saveSettings() {
        val nickname = binding.nicknameInput.text.toString()
        val age = binding.editAgeNumber.text.toString().toIntOrNull() ?: 0
        val gender = binding.genderInput.text.toString()
        val address = binding.addressInput.text.toString()
        val my_style = binding.styleInput.text.toString()

        // Create the UpdateUserRequest object
        val updateUserRequest = UpdateUserRequest(
            nickname = nickname,
            gender = gender,
            age = age,
            address = address,
            mystyle = my_style,
        )

        // Call the updateUser method in the ViewModel
        viewModel.updateUser(updateUserRequest)
    }

}
