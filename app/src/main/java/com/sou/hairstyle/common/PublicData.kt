package com.sou.hairstyle.common

object PublicData {
    const val HAIRSTYLE_INFO_REQUEST_CODE = "HomeFragment_refresh_data"

    val favoriteIdData = ArrayList<Int>()  //保存收藏的发型id
    var loginUid: Int = 0                       //保存当前登录用户的uid
    var loginAvatar: String = ""                    //保存当前登录用户的头像
    var loginNickname: String = ""                    //保存当前登录用户的昵称

    //根据发型id获取是否收藏了发型
    fun getFavoriteStatus(styleId: Int): Boolean {
        return favoriteIdData.contains(styleId)
    }
}