package com.sou.hairstyle.ui.hairstyleInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.network.Response.CommentListDetial
import com.sou.hairstyle.network.Response.CommentListResponse
import com.sou.hairstyle.network.Response.CommentResponse
import com.sou.hairstyle.network.Response.HairInfoResponse
import com.sou.hairstyle.ui.home.Hairstyle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HairstyleInfoViewModel : ViewModel() {
    private val _comments = MutableLiveData<List<CommentItem>>()
    val comments: LiveData<List<CommentItem>> = _comments

    private val _hairInfo = MutableLiveData<Hairstyle>()
    val hairInfo: LiveData<Hairstyle> = _hairInfo

    private var curPage = 1

    init {
//        loadComments()
    }

    fun getCurrentHairStyleInfo(hairstyleId: String) {
        println("_hairInfoItem start")
        HairstyleRepository.getHairStyleInfo(hairstyleId).enqueue(object :
            Callback<HairInfoResponse> {
            override fun onResponse(call: Call<HairInfoResponse>, response: Response<HairInfoResponse>) {
                println("_hairInfoItem code:"+response)
                if (response.isSuccessful) {
                    _hairInfo.value =response.body()?.data
                    println("_hairInfoItem:"+response.body())
                }
            }

            override fun onFailure(call: Call<HairInfoResponse>, t: Throwable) {
                // 处理失败的情况
            }
        })
    }

    fun fetchComments(page: Int = 1, pageSize: Int = 20, hairstyleId: String) {
        println("_hairInfoItems start")
        curPage = page
        HairstyleRepository.getCommentList(page, pageSize, hairstyleId).enqueue(object :
            Callback<CommentListResponse> {
            override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>) {
                println("_hairInfoItems code:"+response)
                if (response.isSuccessful) {
                    _comments.postValue(response.body()?.data?.list?.let { dataFormat(it) })
                    println("_hairInfoItems:"+response.body())
                } else {
                    _comments.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                // 处理失败的情况
                _comments.postValue(emptyList())
            }
        })
    }

    fun loadMore(hairstyleId: String){
        curPage++
        fetchComments(curPage, 20, hairstyleId)
    }

    fun addComment(comment: String, hairstyleIds: String, result: (success: Boolean)-> Unit){
        HairstyleRepository.addComment(comment, hairstyleIds).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()?.code != 1){
                        result.invoke(false)
                    }else
                    {
                        result.invoke(true)
                    }

                } else {
                    // 处理错误响应
                    result.invoke(false)
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                // 网络请求失败
                result.invoke(false)
            }
        })
    }

    private fun loadComments() {
        // Load comments (this is just a placeholder for actual data loading logic)
        _comments.value = listOf(
            CommentItem("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            CommentItem("User2", "Looks awesome!", "https://example.com/image2.png"),
            CommentItem("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            CommentItem("User2", "Looks awesome!", "https://example.com/image2.png"),
            CommentItem("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            CommentItem("User2", "Looks awesome!", "https://example.com/image2.png"),
            CommentItem("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            CommentItem("User2", "Looks awesome!", "https://example.com/image2.png"),
            CommentItem("User1", "This is a great hairstyle!", "https://example.com/image1.png"),
            CommentItem("User2", "Looks awesome!", "https://example.com/image2.png"),
        )
    }

    private fun dataFormat(from: List<CommentListDetial>): List<CommentItem> {
        return from.map { item ->
            CommentItem(
                username = item.user_nickname,
                userProfileImageUrl = item.user_avatar, // If you have an avatar URL in the Hairstyle object, use it here
                content = item.comment,
            )
        }
    }
}