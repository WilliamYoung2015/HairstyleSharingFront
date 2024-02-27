package com.sou.hairstyle.network.Response

data class SmsCodeResponse(
    val sms_no: String, // 短信流水号
    val code: Int,      // 状态码，成功是1
    val message: String // 消息描述
)
