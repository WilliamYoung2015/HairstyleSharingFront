package com.sou.hairstyle.ui.hairstyleInfo

data class CommentItem(
    val id: Int = 0,
    val userId: Int = 0,
    val username: String?,
    val content: String?,
    val userProfileImageUrl: String?
)