package com.sou.hairstyle.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sou.hairstyle.common.SharedPreferencesManager
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.network.Request.UpdateUserRequest
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.network.Response.ResetPasswordResponse
import com.sou.hairstyle.network.Response.User
import com.sou.hairstyle.network.Response.UserUpdateResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import java.io.File


class SettingViewModel(application: Application) : AndroidViewModel(application){

    private var sharedPreferencesManager: SharedPreferencesManager
    private val _userInit = MutableLiveData<User?>()
    val userInit: MutableLiveData<User?> = _userInit

    private val _userSettings = MutableLiveData<Resource<UserUpdateResponse>>()
    val userSettings: LiveData<Resource<UserUpdateResponse>> = _userSettings

    init {
        sharedPreferencesManager =SharedPreferencesManager.getInstance(application)
        loadSettings()
    }

    private fun loadSettings() {
        // Load settings from repository and post value to _userSettings

        var user = sharedPreferencesManager.getUser()
        userInit.value = user
    }

    // Inside SettingViewModel class
    fun updateUser(params: Map<String, String>, avatar: File?) {
        println("updateUser 1111111111")
//        println(updateUserRequest)
        // Assume that you have a repository with the method to update user
        viewModelScope.launch {
            _userSettings.value = Resource.Loading() // 设置加载状态
            HairstyleRepository.updateUser(params, avatar).enqueue(object : retrofit2.Callback<UserUpdateResponse> {
                override fun onResponse(call: Call<UserUpdateResponse>, response: retrofit2.Response<UserUpdateResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        println("updateUser response.body():"+response.body())
                        if(response.body()?.code != 1){
                            println("updateUser response.body11111():"+response.body())
                            _userSettings.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
                        }else
                        {
                            println("updateUser response.body122222():"+response.body())
                            _userSettings.postValue(Resource.Success(response.body()!!)) // 使用postValue，因为这可能在非主线程调用
                        }

                    } else {
                        // 处理错误响应
                        _userSettings.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
                    }
                }

                override fun onFailure(call: Call<UserUpdateResponse>, t: Throwable) {
                    // 网络请求失败
                    _userSettings.postValue(Resource.Error("Network error: ${t.localizedMessage}"))
                }
            })
        }
    }
}
