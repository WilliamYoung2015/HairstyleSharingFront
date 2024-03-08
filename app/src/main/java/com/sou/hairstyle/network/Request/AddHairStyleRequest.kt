package com.sou.hairstyle.network.Request


data class AddHairStyleRequest(
    val desc: String?=null,
    val title: String?=null,
    val tags: String?=null,
    val id: String? = "",
)