package com.sou.hairstyle.network.Response

import com.sou.hairstyle.ui.home.Hairstyle

data class AddHairStyleResponse(val code: Int,
                                val message: String,
                                val data: Hairstyle
)
