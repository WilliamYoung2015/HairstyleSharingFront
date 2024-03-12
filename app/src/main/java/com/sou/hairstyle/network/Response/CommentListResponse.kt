package com.sou.hairstyle.network.Response

data class CommentListResponse (
    val code: Int,
    val message: String,
    val data: CommentListData? = null
)

data class CommentListData (
    val total : Int,
    val list: List<CommentListDetial>? = null
)

data class CommentListDetial (
    val id : Int,
    val user_id : Int,
    val hairstyle_id: String? = null,
    val star_num: String? = null,
    val comment: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val user_nickname: String? = null,
    val user_avatar: String? = null,
)
