package com.sou.hairstyle.network

import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor


class AuthTokenInterceptor(private val getToken: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        println("originalRequest.url:"+originalRequest.url.toString())
        if (originalRequest.url.toString().contains("token")) {
            return chain.proceed(originalRequest)
        }

        // 获取Token，可能是从SharedPreferences或其他存储中
        val token = getToken()

        println("token AuthTokenInterceptor :"+token)
        // 如果Token不存在，则不修改请求
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // 如果Token存在，添加它到请求头中
        val newRequest = originalRequest.newBuilder()
            .header("X-Auth-Token", token)
            .build()

        return chain.proceed(newRequest)
    }
}


object RetrofitClient {
    private lateinit var _token: String
    private const val BASE_URL = "http://47.100.45.214:80/"



    val logging = HttpLoggingInterceptor().apply {
        // 设置日志级别
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestWithHeaders = originalRequest.newBuilder()
                .header("Accept", "application/json")
                .method(originalRequest.method, originalRequest.body)
                .build()
            chain.proceed(requestWithHeaders)
        }
        .addInterceptor(AuthTokenInterceptor(::getToken))
        .addInterceptor(logging)
        .build()


    val instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun saveToken(token: String) {
        RetrofitClient._token = token
        println("token:"+token)
    }
    fun getToken(): String? {
        return _token;
    }

}
