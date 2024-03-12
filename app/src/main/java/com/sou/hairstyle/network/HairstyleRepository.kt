package com.sou.hairstyle.network

import com.sou.hairstyle.common.FileUtil
import com.sou.hairstyle.network.Request.LoginRequest
import com.sou.hairstyle.network.Request.RegisterRequest
import com.sou.hairstyle.network.Request.ResetPasswordRequest
import com.sou.hairstyle.network.Request.SmsCodeRequest
import com.sou.hairstyle.network.Request.UpdateUserRequest
import com.sou.hairstyle.network.Response.AddHairStyleResponse
import com.sou.hairstyle.network.Response.LoginResponse
import com.sou.hairstyle.network.Response.RegisterResponse
import com.sou.hairstyle.network.Response.CommonResponse
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

    fun getHairStylePlaza(page: Int, pageSize: Int): Call<HairInfoResponse> {
        return apiService.getHairStylePlazaData(page, pageSize)
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
    ): Call<CommonResponse> {
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

    fun updateUser(updateUserRequest:UpdateUserRequest,
                 avatar: File?
    ): Call<UserUpdateResponse> {
        val partList = arrayListOf<MultipartBody.Part>()
        partList.add(MultipartBody.Part.createFormData("nickname", updateUserRequest.nickname?: ""))
        partList.add(MultipartBody.Part.createFormData("username", updateUserRequest.username?: ""))
        partList.add(MultipartBody.Part.createFormData("email", updateUserRequest.email?: ""))
        partList.add(MultipartBody.Part.createFormData("gender", updateUserRequest.gender?: ""))
        partList.add(MultipartBody.Part.createFormData("age", (updateUserRequest.age?: 0).toString()))
        partList.add(MultipartBody.Part.createFormData("address", updateUserRequest.address?: ""))
        partList.add(MultipartBody.Part.createFormData("mystyle", updateUserRequest.mystyle?: ""))
        return if (avatar != null && avatar.exists()) {
            val body1 = avatar.asRequestBody(FileUtil.getMediaType(avatar))
            val avatarPart = MultipartBody.Part.createFormData("avatar", avatar.name, body1)
            partList.add(avatarPart)
            apiService.updateUser(partList)
        } else {
            apiService.updateUser(updateUserRequest)
        }
    }


    fun releaseHairStyle(
        params: Map<String, String>,
        pics: List<File>,
    ): Call<AddHairStyleResponse> {
        val partList = arrayListOf<MultipartBody.Part>()
//        val requestBody = MultipartBody.Builder()
        params.forEach { (key, value) ->
//            requestBody.addFormDataPart(key, value)
            partList.add(MultipartBody.Part.createFormData(key, value))
        }
        pics.forEach {
            if (it.exists()) {
                val body1 = it.asRequestBody(FileUtil.getMediaType(it))
                partList.add(MultipartBody.Part.createFormData("images", it.name, body1))
            }
        }
        return apiService.addHairStyle(partList)
    }

    fun favorite(action: String, hairstyleIds: Int): Call<CommonResponse> {
        return apiService.favorite(action, hairstyleIds)
    }
}