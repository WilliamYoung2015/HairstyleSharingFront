package com.sou.hairstyle.ui.hairstyleInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HairstyleInfoViewModel : ViewModel() {
    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    init {
        loadComments()
    }

    private fun loadComments() {
        // Load comments (this is just a placeholder for actual data loading logic)
        _comments.value = listOf(
            Comment("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            Comment("User2", "Looks awesome!", "https://example.com/image2.png"),
            Comment("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            Comment("User2", "Looks awesome!", "https://example.com/image2.png"),
            Comment("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            Comment("User2", "Looks awesome!", "https://example.com/image2.png"),
            Comment("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            Comment("User2", "Looks awesome!", "https://example.com/image2.png"),
            Comment("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            Comment("User2", "Looks awesome!", "https://example.com/image2.png"),
        )
    }
}