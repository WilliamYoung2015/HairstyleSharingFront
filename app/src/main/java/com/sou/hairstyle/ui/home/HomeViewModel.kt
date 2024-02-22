package com.sou.hairstyle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _hairInfoItems = MutableLiveData<List<HairInfoItem>>()
    val hairInfoItems: LiveData<List<HairInfoItem>> = _hairInfoItems

    init {
        loadTestData()
    }
    private fun loadTestData() {
        val testData = listOf(
            HairInfoItem(
                id = "1",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Elegant Bob Cut",
                description = "This bob cut is sleek and elegant, perfect for a modern look.",
                isLiked = false,
                likeCount = 123
            ),
            HairInfoItem(
                id = "2",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Curly Beauty",
                description = "Embrace your curls with this beautiful and bouncy hairstyle.",
                isLiked = false,
                likeCount = 456
            ),
            HairInfoItem(
                id = "3",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                likeCount = 789
            )
            ,
            HairInfoItem(
                id = "4",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                likeCount = 789
            )
            ,
            HairInfoItem(
                id = "5",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                likeCount = 789
            )
        )
        _hairInfoItems.value = testData
    }
}