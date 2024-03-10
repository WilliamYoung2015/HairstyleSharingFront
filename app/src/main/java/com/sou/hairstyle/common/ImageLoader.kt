package com.sou.hairstyle.common

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoader {

    fun loadImage(context: Context, imageUrl: String?, loadView: ImageView) {
        if (imageUrl.isNullOrEmpty()) return
        Glide.with(context)
            .load(getFullUrl(imageUrl))
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_delete)
            .into(loadView)
    }

    fun getFullUrl(url: String): String {
        if (url.startsWith("http")){
            return url
        } else {
            return "http://47.100.45.214$url"
        }
    }
}