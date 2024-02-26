package com.sou.hairstyle.network.Request

data class LoginRequest(
    val phone: String,
    val password: String,
    val sms_no: String?=null,
    val sms_code: String?=null,
)