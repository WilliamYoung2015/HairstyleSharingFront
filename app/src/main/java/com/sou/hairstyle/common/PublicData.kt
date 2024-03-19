package com.sou.hairstyle.common

object PublicData {

    val favoriteIdData = ArrayList<Int>()  //保存收藏的发型id

    //根据发型id获取是否收藏了发型
    fun getFavoriteStatus(styleId: Int): Boolean {
        return favoriteIdData.contains(styleId)
    }
}