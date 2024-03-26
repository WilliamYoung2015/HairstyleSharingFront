package com.sou.hairstyle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sou.hairstyle.common.PublicData
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

    private val _hairInfoFavorite = MutableLiveData<ArrayList<HairInfoItem>>()
    val hairInfoFavorite: LiveData<ArrayList<HairInfoItem>> = _hairInfoFavorite

    private val _hairInfoRelease = MutableLiveData<ArrayList<HairInfoItem>>()
    val hairInfoRelease: LiveData<ArrayList<HairInfoItem>> = _hairInfoRelease

    private val _hasMoreData = MutableLiveData<Boolean>(true)
    val hasMoreData: LiveData<Boolean> = _hasMoreData

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
    fun getFavoriteListData(page: Int = 1, pageSize: Int = 50) {
        HairstyleRepository.getFavoriteListData(page, pageSize).enqueue(object : Callback<HairInfoListResponse> {
            override fun onResponse(call: Call<HairInfoListResponse>, response: Response<HairInfoListResponse>) {
                if (response.isSuccessful) {
                    PublicData.favoriteIdData.clear()
                    response.body()?.data?.list?.map {
                        PublicData.favoriteIdData.add(it.id)
                    }
                    _hairInfoFavorite.postValue(response.body()?.data?.list?.let { dataFormat(it) })
                } else {
                    _hairInfoFavorite.postValue(ArrayList())
                }
            }

            override fun onFailure(call: Call<HairInfoListResponse>, t: Throwable) {
                // 处理失败的情况
                _hairInfoFavorite.postValue(ArrayList())
            }
        })
    }

    //获取发布的发型列表
    fun getReleaseListData(page: Int = 1, pageSize: Int = 50) {
        HairstyleRepository.getHairstyles(page, pageSize).enqueue(object : Callback<HairInfoListResponse> {
            override fun onResponse(call: Call<HairInfoListResponse>, response: Response<HairInfoListResponse>) {
                if (response.isSuccessful) {
                    _hairInfoRelease.postValue(response.body()?.data?.list?.let { dataFormat(it) })
                } else {
                    _hairInfoRelease.postValue(ArrayList())
                }
            }

            override fun onFailure(call: Call<HairInfoListResponse>, t: Throwable) {
                // 处理失败的情况
                _hairInfoRelease.postValue(ArrayList())
            }
        })
    }


    fun loadMoreData(searchTag: String?, listName: String = "") {
        viewModelScope.launch {
            curPage++
            if (searchTag.isNullOrEmpty()){
                when(listName) {
                    "release" -> {
                        getReleaseListData(curPage, 20)
                    }
                    "favorite" -> {
                        getFavoriteListData(curPage, 20)
                    }
                    else -> {
                        fetchHairstyles(curPage, 10)
                    }
                }
            } else {
                searchHairstyles(searchTag, curPage, 20)
            }
        }
    }

    private fun dataFormat(from: ArrayList<Hairstyle>): ArrayList<HairInfoItem> {
        return from.map { item ->
            HairInfoItem(
                id = item.id.toString(),
                imageUrl = item.images.firstOrNull()?.url ?: "",
                userId = item.user_id,
                avatarUrl = item.user_avatar, // If you have an avatar URL in the Hairstyle object, use it here
                title = item.title,
                description = item.desc,
                isLiked = PublicData.getFavoriteStatus(item.id),
                favorite_count = item.favorite_count
            )
        } as ArrayList<HairInfoItem>
    }

    fun favorite(action: String, hairstyleIds: Int, result: (success: Boolean)-> Unit){
        HairstyleRepository.favorite(action, hairstyleIds).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()?.code != 1){
                        result.invoke(false)
                    } else {
                        //更新本地维护的收藏列表
                        if (action == "bind" && !PublicData.favoriteIdData.contains(hairstyleIds)) {
                            PublicData.favoriteIdData.add(hairstyleIds)
                        } else if (action == "unbind" && PublicData.favoriteIdData.contains(hairstyleIds)){
                            PublicData.favoriteIdData.remove(hairstyleIds)
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

    //删除发型
    fun deleteHairStyle(hairstyleIds: Int, result: (success: Boolean)-> Unit){
        HairstyleRepository.deleteHairStyle(hairstyleIds).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()?.code != 1){
                        result.invoke(false)
                    } else {
                        //更新本地维护的收藏列表
                        if (PublicData.favoriteIdData.contains(hairstyleIds)){
                            PublicData.favoriteIdData.remove(hairstyleIds)
                        }
                        result.invoke(true)
                    }

                } else {
                    // 处理错误响应
                    result.invoke(false)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                result.invoke(false)
            }
        })
    }
}