package com.sou.hairstyle.common

import android.content.Context
import com.google.gson.Gson
import com.sou.hairstyle.network.Response.User

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()


    companion object {
        private const val USER_KEY = "user"
        private const val TOKEN_KEY = "token"

        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context.applicationContext).also { instance = it }
            }
        }
    }
    fun saveUser(user: User) {
        val userJson = Gson().toJson(user)
        editor.putString(USER_KEY, userJson).apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER_KEY, null)
        return if (userJson != null) Gson().fromJson(userJson, User::class.java) else null
    }

    fun clearUser() {
        editor.remove(USER_KEY).apply()
    }

    fun isLoggedIn(): Boolean {
        return getUser() != null
    }

    fun saveToken(token: String) {
        editor.putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        editor.remove(TOKEN_KEY).apply()
    }
}
