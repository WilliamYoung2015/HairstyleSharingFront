package com.sou.hairstyle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sou.hairstyle.common.SharedPreferencesManager
import com.sou.hairstyle.databinding.ActivityMainBinding
import com.sou.hairstyle.ui.userInfo.UserInfoFragment
import com.sou.hairstyle.ui.release.AddHairStyleActivity


class MainActivity : AppCompatActivity(), UserInfoFragment.FragmentUserInfoListener {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化Fresco
        Fresco.initialize(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this)

        val navView: BottomNavigationView = binding.navView

        val colorStateList = ContextCompat.getColorStateList(this, R.color.navigation_item_color)
        navView.itemIconTintList = colorStateList
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.navigation_dashboard).setOnMenuItemClickListener {
            startActivity(Intent(this, AddHairStyleActivity::class.java))
            return@setOnMenuItemClickListener true
        }
        supportActionBar?.hide()
    }

    override fun navigateToActivityAndFinish(intent: Intent) {
        sharedPreferencesManager.clearUser()
        sharedPreferencesManager.clearToken()
        startActivity(intent)
        finish()
    }
}