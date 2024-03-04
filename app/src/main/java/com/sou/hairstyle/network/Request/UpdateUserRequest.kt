package com.sou.hairstyle.network.Request

import java.io.File

data class UpdateUserRequest(
    val nickname: String?=null,
    val username: String?=null,
    val email: String?=null,
    val gender: String?,
    val age: Int?,
    val address: String?=null,
    val mystyle: String?=null,
    val avatar: File?=null, // Assuming avatar is a String path or URL for simplicity
)