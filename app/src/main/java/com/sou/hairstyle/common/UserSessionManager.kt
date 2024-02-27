package com.sou.hairstyle.common

import com.sou.hairstyle.network.Response.User

object UserSessionManager {
    private var user: User? = null

    fun isLoggedIn(): Boolean = user != null

    fun loginUser(user: User) {
        this.user = user
    }

    fun logoutUser() {
        user = null
    }

    fun getUser(): User? = user
}
