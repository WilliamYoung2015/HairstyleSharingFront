package com.sou.hairstyle.ui.release

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sou.hairstyle.R

class AddHairStyleActivity: AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "发布新发型"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.black_overlay))
        setContentView(R.layout.activity_release_hair_style)
    }
}