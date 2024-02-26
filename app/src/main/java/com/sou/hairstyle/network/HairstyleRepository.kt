package com.sou.hairstyle.network

import com.sou.hairstyle.network.Request.RegisterRequest
import com.sou.hairstyle.network.Request.SmsCodeRequest
import com.sou.hairstyle.network.Response.RegisterResponse
import com.sou.hairstyle.network.Response.SmsCodeResponse
import com.sou.hairstyle.network.Response.TokenResponse
import com.sou.hairstyle.ui.home.HairInfoItem
import com.sou.hairstyle.ui.home.HairInfoResponse
import retrofit2.Call

object HairstyleRepository {

    private val apiService: ApiService by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    fun getHairstyles(page: Int, pageSize: Int): Call<HairInfoResponse> {
        return apiService.getHairInfosData(page, pageSize)
    }

    fun getToken(): Call<TokenResponse> {
        return apiService.getToken()
    }

    fun sendSmsCode(phone:String):Call<SmsCodeResponse>{
        val sendSmsRequest = SmsCodeRequest(phone = phone)
        return  apiService.sendSmsCode(sendSmsRequest)
    }

    fun registerUser(
        phone: String,
        smsNo: String,
        smsCode: String,
        password: String,
    ): Call<RegisterResponse> {
        val registerRequest = RegisterRequest(phone, smsNo, smsCode, password)
        return apiService.registerUser(registerRequest)
    }
}