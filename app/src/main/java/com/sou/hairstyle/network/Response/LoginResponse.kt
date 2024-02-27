package com.sou.hairstyle.network.Response

data class LoginResponse(
    val code: Int,
    val message: String,
    val data: UserData
)

data class UserData(
    val user: User
)