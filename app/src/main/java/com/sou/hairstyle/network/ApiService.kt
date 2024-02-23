package com.sou.hairstyle.network

import com.sou.hairstyle.network.Response.TokenResponse
import com.sou.hairstyle.ui.home.HairInfoResponse
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("hairstyle/list")
    fun getHairInfosData(
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int): Call<HairInfoResponse>

    @GET("token")
    fun getToken(): Call<TokenResponse>
}
