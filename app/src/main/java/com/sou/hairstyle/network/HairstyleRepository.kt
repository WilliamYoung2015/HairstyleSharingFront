package com.sou.hairstyle.network

import com.sou.hairstyle.network.Request.LoginRequest
import com.sou.hairstyle.network.Request.RegisterRequest
import com.sou.hairstyle.network.Request.ResetPasswordRequest
import com.sou.hairstyle.network.Request.SmsCodeRequest
import com.sou.hairstyle.network.Request.UpdateUserRequest
import com.sou.hairstyle.network.Response.AddHairStyleResponse
import com.sou.hairstyle.network.Response.LoginResponse
import com.sou.hairstyle.network.Response.RegisterResponse
import com.sou.hairstyle.network.Response.ResetPasswordResponse
import com.sou.hairstyle.network.Response.SmsCodeResponse
import com.sou.hairstyle.network.Response.TokenResponse
import com.sou.hairstyle.network.Response.UserUpdateResponse
import com.sou.hairstyle.ui.home.HairInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.File

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

    fun getUserInfo(): Call<LoginResponse> {
        return apiService.getUserInfo()
    }

    fun sendSmsCode(phone: String): Call<SmsCodeResponse> {
        val sendSmsRequest = SmsCodeRequest(phone = phone)
        return apiService.sendSmsCode(sendSmsRequest)
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

    fun loginUser(
        phone: String,
        password: String,
    )
            : Call<LoginResponse> {
        val loginRequest = LoginRequest(phone = phone, password = password)
        return apiService.loginUser(loginRequest)
    }

    fun resetPassword(
        phone: String,
        smsNo: String,
        smsCode: String,
        password: String,
    ): Call<ResetPasswordResponse> {
        val resetPasswordRequest = ResetPasswordRequest(phone, smsNo, smsCode, password)
        return apiService.resetPassword(resetPasswordRequest)
    }

//    fun updateUser(
//        nickname: String?,
//        username: String?,
//        email: String?,
//        gender: String?,
//        age: Int?,
//        address: String?,
//        mystyle: String?,
//        avatar: String?
//    ): Call<UserUpdateResponse> {
//        val updateUserRequest = UpdateUserRequest(
//            nickname = nickname,
//            username = username,
//            email = email,
//            gender = gender,
//            age = age,
//            address = address,
//            mystyle = mystyle,
//            avatar = avatar
//        )
//        return apiService.updateUser(updateUserRequest)
//    }

    fun updateUser(updateUserRequest:UpdateUserRequest
    ): Call<UserUpdateResponse> {
        return apiService.updateUser(updateUserRequest)
    }


    fun releaseHairStyle(
        map: Map<String, String>,
        paths: List<String>,
    ): Call<AddHairStyleResponse> {
        val partList = arrayListOf<MultipartBody.Part>()
        map.forEach { (key, value) ->
            partList.add(MultipartBody.Part.createFormData(key, value))
        }
        paths.forEach {
            val file1 = File(it)
            if (file1.exists()) {
                val body1 = file1.asRequestBody(MultipartBody.FORM)
                partList.add(MultipartBody.Part.createFormData("images", file1.name, body1))
            }
        }
        return apiService.addHairStyle(partList)
    }
}