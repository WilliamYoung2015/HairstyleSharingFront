package com.sou.hairstyle.network.Response

data class ResetPasswordResponse(
    val user: User,
    val code: Int,      // 状态码，成功是1
    val message: String // 消息描述
)
