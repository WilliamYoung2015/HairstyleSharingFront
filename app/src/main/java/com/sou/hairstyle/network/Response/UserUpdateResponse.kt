package com.sou.hairstyle.network.Response

data class UserUpdateResponse(
    val code: Int,
    val message: String,
    val data: User
)