package com.sou.hairstyle.network.Response

import com.sou.hairstyle.ui.home.Hairstyle

data class HairInfoResponse(
    val data: Hairstyle,
    val code: Int,      // 状态码，成功是1
    val message: String // 消息描述
)
