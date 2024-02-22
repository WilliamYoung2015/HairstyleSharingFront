package com.sou.hairstyle.network

import com.sou.hairstyle.ui.home.HairInfoItem
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("hairstyle/list")
    fun getHairInfosData(
        @Header("X-Auth-Token") authToken: String,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int): Call<List<HairInfoItem>>
}
