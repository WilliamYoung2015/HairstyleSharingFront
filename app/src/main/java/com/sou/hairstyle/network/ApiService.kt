package com.sou.hairstyle.network

import com.sou.hairstyle.network.Request.FavoriteRequest
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
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("hairstyle/list")
    fun getHairInfosData(
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int): Call<HairInfoResponse>

    @GET("token")
    fun getToken(): Call<TokenResponse>

    @POST("sms/send")
    fun sendSmsCode(@Body sendSmsRequest: SmsCodeRequest): Call<SmsCodeResponse>

    @POST("user/register")
    fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @GET("user")
    fun getUserInfo(): Call<LoginResponse>

    @POST("user/login")
    fun loginUser(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("user/reset_password")
    fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Call<CommonResponse>

    @PUT("favorite")
    fun favorite(
        @Query("action") action: String,
        @Query("hairstyle_ids") hairstyleIds: Int
    ): Call<CommonResponse>

    @Multipart
    @PUT("user")
    fun updateUser(
        @Part partList: List<MultipartBody.Part>,
    ): Call<UserUpdateResponse>

    @PUT("user")
    fun updateUser(
        @Body updateUserRequest:UpdateUserRequest
    ): Call<UserUpdateResponse>


    @Multipart
    @PUT("hairstyle")
    fun addHairStyle(
        @Part partList: List<MultipartBody.Part>,
    ): Call<AddHairStyleResponse>
}
