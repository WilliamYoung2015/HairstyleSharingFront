package com.sou.hairstyle.ui.home

data class HairInfoItem(
    val id: String,
    val imageUrl: String,
    val avatarUrl: String,
    val title: String,
    val description: String,
    var isLiked: Boolean,
    var likeCount: Int
)