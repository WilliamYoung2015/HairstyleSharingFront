package com.sou.hairstyle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.network.Response.CommonResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _hairInfoResponse = MutableLiveData<HairInfoListResponse>()

    private val _hairInfoItems = MutableLiveData<ArrayList<HairInfoItem>>()
    val hairInfoItems: LiveData<ArrayList<HairInfoItem>> = _hairInfoItems

    private val _hasMoreData = MutableLiveData<Boolean>(true)
    val hasMoreData: LiveData<Boolean> = _hasMoreData

    private val _favoriteIdData = ArrayList<Int>()  //保存收藏的发型id
    private var curPage = 1

    fun fetchHairstyles(page: Int = 1, pageSize: Int = 20) {
        this.curPage = page
        println("_hairInfoItems start")
        HairstyleRepository.getHairStylePlaza(page, pageSize).enqueue(object : Callback<HairInfoListResponse> {
            override fun onResponse(call: Call<HairInfoListResponse>, response: Response<HairInfoListResponse>) {
                println("_hairInfoItems code:"+response)
                if (response.isSuccessful) {
                    _hairInfoResponse.postValue(response.body())
                    _hairInfoItems.postValue(response.body()?.data?.list?.let { dataFormat(it) })
                    if (response.body()?.data?.list?.isNullOrEmpty() == true) {
                        _hasMoreData.value = false // 没有更多数据可以加载
                    } else {
                        _hasMoreData.value = false
                    }
                    println("_hairInfoItems:"+response.body())
                } else {
                    _hairInfoItems.postValue(ArrayList())
                }
            }

            override fun onFailure(call: Call<HairInfoListResponse>, t: Throwable) {
                // 处理失败的情况
                _hairInfoItems.postValue(ArrayList())
            }
        })
    }

    fun searchHairstyles(tag: String, page: Int = 1, pageSize: Int = 20) {
        println("_hairInfoItems start")
        HairstyleRepository.getHairStylePlazaWithTag(tag, page, pageSize).enqueue(object : Callback<HairInfoListResponse> {
            override fun onResponse(call: Call<HairInfoListResponse>, response: Response<HairInfoListResponse>) {
                println("_hairInfoItems code:"+response)
                if (response.isSuccessful) {
                    _hairInfoResponse.postValue(response.body())
                    _hairInfoItems.postValue(response.body()?.data?.list?.let { dataFormat(it) })
                    println("_hairInfoItems:"+response.body())
                } else {
                    _hairInfoItems.postValue(ArrayList())
                }
            }

            override fun onFailure(call: Call<HairInfoListResponse>, t: Throwable) {
                // 处理失败的情况
                _hairInfoItems.postValue(ArrayList())
            }
        })
    }

    //获取收藏的发型列表
    private fun getFavoriteListData(page: Int = 1, pageSize: Int = 50) {
        HairstyleRepository.getFavoriteListData(page, pageSize).enqueue(object : Callback<HairInfoListResponse> {
            override fun onResponse(call: Call<HairInfoListResponse>, response: Response<HairInfoListResponse>) {
                if (response.isSuccessful) {
                    _favoriteIdData.clear()
                    response.body()?.data?.list?.map {
                        _favoriteIdData.add(it.id)
                    }
                }
            }

            override fun onFailure(call: Call<HairInfoListResponse>, t: Throwable) {
                // 处理失败的情况
            }
        })
    }

    init {
        initData()
    }

    fun initData(){
        getFavoriteListData()
//        fetchHairstyles()
    }

    //根据发型id获取是否收藏了发型
    private fun getFavoriteStatus(styleId: Int): Boolean {
        return _favoriteIdData.contains(styleId)
    }

    fun loadMoreData(searchTag: String?) {
        viewModelScope.launch {
            curPage++
            if (searchTag.isNullOrEmpty()){
                fetchHairstyles(curPage, 10)
            } else {
                searchHairstyles(searchTag, curPage, 20)
            }

//            val moreData: List<HairInfoItem> = fetchMoreData() // 异步获取更多数据
//            if (moreData.isEmpty()) {
//                _hasMoreData.value = false // 没有更多数据可以加载
//            } else {
//                _hasMoreData.value = false
//            }
        }
    }

    private fun dataFormat(from: ArrayList<Hairstyle>): ArrayList<HairInfoItem> {
        return from.map { item ->
            HairInfoItem(
                id = item.id.toString(),
                imageUrl = item.images.firstOrNull()?.url ?: "",
                avatarUrl = "", // If you have an avatar URL in the Hairstyle object, use it here
                title = item.title,
                description = item.desc,
                isLiked = getFavoriteStatus(item.id),
                star_sum = item.star_sum
            )
        } as ArrayList<HairInfoItem>
    }


    private fun reverseDataFormat(from: List<HairInfoItem>): ArrayList<Hairstyle> {
        return from.map { item ->
            Hairstyle(
                id = item.id.toInt(),
                user_id = 0, // Placeholder since HairInfoItem doesn't have this
                title = item.title,
                desc = item.description,
                tags = "", // Placeholder since HairInfoItem doesn't have this
                star_sum = item.star_sum!!,
                comment_count = 0, // Placeholder since HairInfoItem doesn't have this
                favorite_count = 0, // Placeholder since HairInfoItem doesn't have this
                created_at = "", // Placeholder since HairInfoItem doesn't have this
                updated_at = "", // Placeholder since HairInfoItem doesn't have this
                images = arrayListOf(
                    Image(
                        id = 0, // Placeholder since HairInfoItem doesn't have this
                        user_id = 0, // Placeholder since HairInfoItem doesn't have this
                        hairstyle_id = item.id.toInt(),
                        url = item.imageUrl,
                        privacy_url = "", // Placeholder since HairInfoItem doesn't have this
                        created_at = "", // Placeholder since HairInfoItem doesn't have this
                        updated_at = "" // Placeholder since HairInfoItem doesn't have this
                    )
                )
            )
        } as ArrayList<Hairstyle>
    }



    private fun loadTestData() {
        val testData = arrayListOf(
            HairInfoItem(
                id = "1",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Elegant Bob Cut",
                description = "This bob cut is sleek and elegant, perfect for a modern look.",
                isLiked = false,
                star_sum = 123
            ),
            HairInfoItem(
                id = "2",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Curly Beauty",
                description = "Embrace your curls with this beautiful and bouncy hairstyle.",
                isLiked = false,
                star_sum = 456
            ),
            HairInfoItem(
                id = "3",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                star_sum = 789
            )
            ,
            HairInfoItem(
                id = "4",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                star_sum = 789
            )
            ,
            HairInfoItem(
                id = "5",
                imageUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                avatarUrl = "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
                title = "Straight and Chic",
                description = "A straight hairstyle that is both chic and easy to maintain.",
                isLiked = false,
                star_sum = 789
            )
        )

        val hairstyles = reverseDataFormat(testData)

        val hairInfoList = dataFormat(hairstyles)
        _hairInfoItems.value = hairInfoList
    }

    fun favorite(action: String, hairstyleIds: Int, result: (success: Boolean)-> Unit){
        HairstyleRepository.favorite(action, hairstyleIds).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()?.code != 1){
                        result.invoke(false)
                    }else
                    {
                        //更新本地维护的收藏列表
                        if (action == "bind" && !_favoriteIdData.contains(hairstyleIds)) {
                            _favoriteIdData.add(hairstyleIds)
                        } else if (action == "unbind" && _favoriteIdData.contains(hairstyleIds)){
                            _favoriteIdData.remove(hairstyleIds)
                        }
                        result.invoke(true)
                    }

                } else {
                    // 处理错误响应
                    result.invoke(false)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                // 网络请求失败
                result.invoke(false)
            }
        })
    }
}