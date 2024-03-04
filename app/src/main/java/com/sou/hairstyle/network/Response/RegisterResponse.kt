package com.sou.hairstyle.network.Response

data class RegisterResponse(
    val user: User,
    val code: Int,      // 状态码，成功是1
    val message: String // 消息描述
)

data class User(
    val id: Int,
    val nickname: String?=null,
    val username: String?=null,
    val email: String?=null,
    val phone: String?=null,
    val gender: String?=null,
    val age: Int?=null,
    val address: String?=null,
    val mystyle: String?=null,
    val avatar: String?=null,
    val last_login: String?=null,
    val created_at: String?=null,
    val updated_at: String?=null,
    // 如果还有其他用户相关数据，请根据实际情况添加属性
)
