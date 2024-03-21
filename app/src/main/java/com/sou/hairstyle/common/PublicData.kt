package com.sou.hairstyle.common

object PublicData {
    const val HAIRSTYLE_INFO_REQUEST_CODE = "HomeFragment_refresh_data"

    val favoriteIdData = ArrayList<Int>()  //保存收藏的发型id

    //根据发型id获取是否收藏了发型
    fun getFavoriteStatus(styleId: Int): Boolean {
        return favoriteIdData.contains(styleId)
    }
}