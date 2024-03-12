package com.sou.hairstyle.network.Response


data class CommentResponse(
    val data: CommentData,
    val code: Int,      // 状态码，成功是1
    val message: String // 消息描述
)

data class CommentData (
    val id : Int,
    val user_id : Int,
    val hairstyle_id: String? = null,
    val star_num: String? = null,
    val comment: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
)