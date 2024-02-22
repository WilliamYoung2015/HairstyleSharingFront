package com.sou.hairstyle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sou.hairstyle.network.ApiService
import com.sou.hairstyle.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HairstyleRepository {

    private val apiService: ApiService by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    fun getHairstyles(authToken: String, page: Int, pageSize: Int): Call<List<HairInfoItem>> {
        return apiService.getHairInfosData(authToken, page, pageSize)
    }
}

class HomeViewModel : ViewModel() {
    private val hairstyleRepository = HairstyleRepository()

    private val _hairInfoItems = MutableLiveData<List<HairInfoItem>>()
    val hairInfoItems: LiveData<List<HairInfoItem>> = _hairInfoItems

    private val _hasMoreData = MutableLiveData<Boolean>(true)
    val hasMoreData: LiveData<Boolean> = _hasMoreData
    fun fetchHairstyles(authToken: String, page: Int = 1, pageSize: Int = 20) {
        hairstyleRepository.getHairstyles(authToken, page, pageSize).enqueue(object : Callback<List<HairInfoItem>> {
            override fun onResponse(call: Call<List<HairInfoItem>>, response: Response<List<HairInfoItem>>) {
                if (response.isSuccessful) {
                    _hairInfoItems.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<HairInfoItem>>, t: Throwable) {
                // 处理失败的情况
            }
        })
    }

    init {
        loadTestData()
    }

    suspend fun fetchMoreData(): List<HairInfoItem> {
        // 这里是模拟从数据源获取数据的逻辑
        // 在实际应用中，这里可能是一个网络请求或数据库查询
        return listOf(
            HairInfoItem(
                id = "6",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Elegant Bob Cut",
                description = "This bob cut is sleek and elegant, perfect for a modern look.",
                isLiked = false,
                likeCount = 123
            ),
            HairInfoItem(
                id = "7",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Curly Beauty",
                description = "Embrace your curls with this beautiful and bouncy hairstyle.",
                isLiked = false,
                likeCount = 456
            ),
            HairInfoItem(
                id = "8",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                likeCount = 789
            )
            ,
            HairInfoItem(
                id = "9",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                likeCount = 789
            )
            ,
            HairInfoItem(
                id = "10",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                likeCount = 789
            )
        )
    }

    fun loadMoreData() {
        viewModelScope.launch {
            val moreData: List<HairInfoItem> = fetchMoreData() // 异步获取更多数据
            if (moreData.isEmpty()) {
                _hasMoreData.value = false // 没有更多数据可以加载
            } else {
                _hasMoreData.value = false
//                val currentData: List<HairInfoItem> = _hairInfoItems.value ?: emptyList()
//                _hairInfoItems.postValue(currentData + moreData) // 合并当前数据和新加载的数据
            }
        }
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