package com.sou.hairstyle.network.Response

data class RegisterResponse(
    val user: User,
    val code: Int,      // 状态码，成功是1
    val message: String // 消息描述
)

data class User(
    val id: Int,
    val nickname: String,
    val username: String,
    val email: String,
    val phone: String,
    val gender: String,
    val age: Int,
    val address: String,
    val my_style: String,
    val avatar: String,
    val last_login: String,
    val created_at: String,
    val updated_at: String
    // 如果还有其他用户相关数据，请根据实际情况添加属性
)
