package com.sou.hairstyle.ui.home

data class HairInfoItem(
    val id: String,
    val imageUrl: String,
    val avatarUrl: String,
    val title: String,
    val description: String,
    var isLiked: Boolean,
    var star_sum: Int=0,
    var comment_count: Int=0,
    var favorite_count:Int=0,
)


data class HairInfoListResponse(
    val code: Int,
    val message: String,
    val data: Data
)

data class Data(
    val total: Int,
    val list: List<Hairstyle>
)

data class Hairstyle(
    val id: Int,
    val user_id: Int,
    val title: String,
    val desc: String,
    val tags: String, // 如果tags是以逗号分隔的，可能需要转换为List<String>
    val star_sum: Int,
    val comment_count: Int,
    val favorite_count: Int,
    val created_at: String,
    val updated_at: String,
    val images: List<Image>
)

data class Image(
    val id: Int,
    val user_id: Int,
    val hairstyle_id: Int,
    val url: String,
    val privacy_url: String?,
    val created_at: String,
    val updated_at: String
)