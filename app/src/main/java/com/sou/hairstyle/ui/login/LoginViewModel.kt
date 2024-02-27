package com.sou.hairstyle.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sou.hairstyle.common.UserSessionManager
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.network.Request.LoginRequest
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.network.Response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginStatus = MutableLiveData<Resource<LoginResponse>>()
    val loginStatus: LiveData<Resource<LoginResponse>> = _loginStatus

    fun login(phone: String, password: String) {
//        val loginRequest = LoginRequest(phone, password)
        _loginStatus.value = Resource.Loading()

        viewModelScope.launch {
            HairstyleRepository.loginUser(phone, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null&& response.body()?.code == 1) {
                        println("~~~~~~~~~~~~~~~~~")
                        println(response.body())
                        val userData = response.body()?.data?.user
                        userData?.let {
                            _loginStatus.postValue(Resource.Success(response.body()!!))
                        }

//                        if(response.body()?.code != 1){
//                            _loginStatus.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
//                        }else {
//                            _loginStatus.postValue(Resource.Success(response.body()!!))
//                        }
                    } else {
                        _loginStatus.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
//                        _loginStatus.postValue(Resource.Error("Login failed: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _loginStatus.postValue(Resource.Error("Network error: ${t.localizedMessage}"))
                }
            })
        }
    }
}