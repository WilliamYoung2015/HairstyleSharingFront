package com.sou.hairstyle.network.Request

data class RegisterRequest(
    val phone: String,
    val sms_no: String,    // 短信流水号
    val sms_code: String,  // 短信验证码
    val password: String,
//    val username: String,  // 可能为手机号或自定义用户名
//    val nickname: String   // 用户昵称
)
