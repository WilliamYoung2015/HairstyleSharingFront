package com.sou.hairstyle.ui.userInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sou.hairstyle.network.Response.User

class UserInfoViewModel : ViewModel() {
    private val _userData = MutableLiveData<User?>()
    val userData: MutableLiveData<User?> = _userData

    fun initUserInfo(user: User){
        _userData.value = user
    }

    fun logout() {
        // Clear user data or handle logout logic
        _userData.value = null
        // Clear session, navigate to login screen, etc.
    }
}