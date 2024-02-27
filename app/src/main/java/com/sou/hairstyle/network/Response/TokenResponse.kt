package com.sou.hairstyle.network.Response

data class TokenResponse(
    val code: Int,
    val message: String,
    val data: TokenData
)

data class TokenData(
    val token: String
)
