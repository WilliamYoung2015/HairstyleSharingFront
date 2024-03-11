package com.sou.hairstyle.network.Response

/**
 * 通用无数据的Response
 */
data class CommonResponse(
    val user: User,
    val code: Int,      // 状态码，成功是1
    val message: String // 消息描述
)
