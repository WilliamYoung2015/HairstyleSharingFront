package com.sou.hairstyle.network

import com.sou.hairstyle.network.Response.TokenResponse
import com.sou.hairstyle.ui.home.HairInfoItem
import com.sou.hairstyle.ui.home.HairInfoResponse
import retrofit2.Call

class HairstyleRepository {

    private val apiService: ApiService by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    fun getHairstyles(page: Int, pageSize: Int): Call<HairInfoResponse> {
        return apiService.getHairInfosData(page, pageSize)
    }

    fun getToken(): Call<TokenResponse> {
        return apiService.getToken()
    }
}