package com.sou.hairstyle

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.sou.hairstyle.common.SharedPreferencesManager
import com.sou.hairstyle.databinding.ActivitySplashBinding
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.network.Response.TokenResponse
import com.sou.hairstyle.network.RetrofitClient
import com.sou.hairstyle.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var fullscreenContent: TextView

    private var isFullscreen: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.recordButton.setOnClickListener {
            val sharedPreferencesManager = SharedPreferencesManager.getInstance(applicationContext)
//            sharedPreferencesManager.clearUser()//清理用户数据测试
            println("sharedPreferencesManager user info:")
            println(sharedPreferencesManager.getUser())
            if (sharedPreferencesManager.isLoggedIn()) {
                // 用户已登录，可以获取用户信息ß
                val user = sharedPreferencesManager.getUser()
                // 处理已登录的逻辑
                // 用户已登录，跳转到MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // 用户未登录
                // 处理未登录的逻辑
                // 用户未登录，跳转到RegisterActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish() // 如果你不想用户能够返回到这个SplashActivity

        }

        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }



// To retrieve a token
        val token = SharedPreferencesManager.getInstance(this).getToken()
        if(token.isNullOrEmpty()){
            this.fetchToken()
        }else
        {
            RetrofitClient.saveToken(token)
        }

    }

    private fun fetchToken() {
        val _context = this;
        val tokenLiveData: MutableLiveData<TokenResponse> = MutableLiveData()
        println("tokenLiveData start")
        HairstyleRepository.getToken().enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                println("tokenLiveData onResponse")
                if (response.isSuccessful) {
                    println("tokenLiveData onResponse isSuccessful"+response.body())
                    tokenLiveData.postValue(response.body())
                    println(tokenLiveData.value?.code)
                    println("tokenLiveData:"+ (response.body()?.data?.token ?: "??"))
                    response.body()?.data?.token?.let {
                        RetrofitClient.saveToken(it)
                        SharedPreferencesManager.getInstance(_context).saveToken(it)
                    };
                } else {
                    // 处理错误情况
                    println("tokenLiveData onResponse error")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // 处理请求失败的情况，比如网络问题
                println("tokenLiveData onFailure")
            }
        })
    }

}