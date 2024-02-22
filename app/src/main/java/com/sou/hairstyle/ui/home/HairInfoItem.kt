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

data class HairInfoResponse(
    val code: Int,
    val message: String,
    val data: DataObject
)

data class DataObject(
    val total: Int,
    // 假设data里还有一个名为items的数组
    val items: List<HairInfoItem>
)