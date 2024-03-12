package com.sou.hairstyle.ui.hairstyleInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.ui.home.HairInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HairstyleInfoViewModel : ViewModel() {
    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    init {
        loadComments()
    }

    fun fetchComments(page: Int = 1, pageSize: Int = 20,hairstyleId:Int) {
        println("_hairInfoItems start")
        HairstyleRepository.getCommentList(page, pageSize, hairstyleId).enqueue(object :
            Callback<HairInfoResponse> {
            override fun onResponse(call: Call<HairInfoResponse>, response: Response<HairInfoResponse>) {
                println("_hairInfoItems code:"+response)
                if (response.isSuccessful) {
                    _comments.postValue(response.body()?.data?.list?.let { dataFormat(it) })
                    println("_hairInfoItems:"+response.body())
                }
            }

            override fun onFailure(call: Call<HairInfoResponse>, t: Throwable) {
                // 处理失败的情况
            }
        })
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