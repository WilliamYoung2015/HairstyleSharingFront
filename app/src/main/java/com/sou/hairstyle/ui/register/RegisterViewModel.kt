package com.sou.hairstyle.ui.register

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.network.Response.RegisterResponse
import com.sou.hairstyle.network.Response.SmsCodeResponse
import kotlinx.coroutines.launch
import retrofit2.Call

class RegisterViewModel : ViewModel() {

    private val _registrationStatus = MutableLiveData<Resource<RegisterResponse>>()
    val registrationStatus: LiveData<Resource<RegisterResponse>> = _registrationStatus

    private val _smsStatus = MutableLiveData<Resource<SmsCodeResponse>>()
    val smsStatus: LiveData<Resource<SmsCodeResponse>> = _smsStatus

    // 用来模拟倒计时的LiveData
    private val _countdownTime = MutableLiveData<Long>()
    val countdownTime: LiveData<Long> = _countdownTime
    private var countdownTimer: CountDownTimer? = null

    fun sendVerificationCode(phone: String) {
        _smsStatus.value = Resource.Loading()
        viewModelScope.launch {
            try {
                HairstyleRepository.sendSmsCode(phone).enqueue(object : retrofit2.Callback<SmsCodeResponse> {
                    override fun onResponse(
                        call: Call<SmsCodeResponse>,
                        response: retrofit2.Response<SmsCodeResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            if(response.body()?.code != 1){
                                _smsStatus.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
                            }else {
                                _smsStatus.postValue(Resource.Success(response.body()!!)) // 使用postValue，因为这可能在非主线程调用
                            }
                        }
                    }

                    override fun onFailure(call: Call<SmsCodeResponse>, t: Throwable) {
                        // 网络请求失败
                        _smsStatus.postValue(Resource.Error("Network error: ${t.localizedMessage}"))
                    }
                })
                // 启动倒计时
                startCountdownTimer()
            } catch (e: Exception) {
                _smsStatus.value = Resource.Error(e.localizedMessage ?: "An error occurred", null)
            }
        }
    }

    fun register(phone: String, smsNo: String,smsCode: String, password: String) {
        viewModelScope.launch {
            _registrationStatus.value = Resource.Loading() // 设置加载状态
            HairstyleRepository.registerUser(phone, smsNo, smsCode, password).enqueue(object : retrofit2.Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: retrofit2.Response<RegisterResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        println("response.body():"+response.body())
                        if(response.body()?.code != 1){
                            println("response.body11111():"+response.body())
                            _registrationStatus.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
                        }else
                        {
                            println("response.body122222():"+response.body())
                            _registrationStatus.postValue(Resource.Success(response.body()!!)) // 使用postValue，因为这可能在非主线程调用
                        }

                    } else {
                        // 处理错误响应
                        _registrationStatus.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    // 网络请求失败
                    _registrationStatus.postValue(Resource.Error("Network error: ${t.localizedMessage}"))
                }
            })
        }
    }

    private fun startCountdownTimer() {
        // 这里应该启动一个倒计时器，并定期更新_countdownTime
        countdownTimer?.cancel() // 取消之前的计时器
        countdownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _countdownTime.postValue(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                _countdownTime.postValue(0)
            }
        }.also {
            it.start() // 启动计时器
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownTimer?.cancel() // 清除计时器
    }
}