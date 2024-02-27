package com.sou.hairstyle.common

import android.content.Context
import com.google.gson.Gson
import com.sou.hairstyle.network.Response.User

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context.applicationContext).also { instance = it }
            }
        }
    }
    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString("user", Gson().toJson(user))
        editor.apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString("user", null)
        return if (userJson != null) {
            Gson().fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun clearUser() {
        sharedPreferences.edit().remove("user").apply()
    }

    fun isLoggedIn(): Boolean {
        return getUser() != null
    }
}
