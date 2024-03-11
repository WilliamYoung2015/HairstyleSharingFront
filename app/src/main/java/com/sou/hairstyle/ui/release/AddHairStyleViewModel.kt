package com.sou.hairstyle.ui.release

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sou.hairstyle.network.HairstyleRepository
import com.sou.hairstyle.network.Request.AddHairStyleRequest
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.network.Response.AddHairStyleResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import java.io.File

class AddHairStyleViewModel: ViewModel() {
    private val _uploadStatus = MutableLiveData<Resource<AddHairStyleResponse>>()
    val uploadStatus: LiveData<Resource<AddHairStyleResponse>> = _uploadStatus
    fun addHairStyle(
        params: Map<String, String>,
                     pics: List<File>){
        _uploadStatus.value = Resource.Loading()
        viewModelScope.launch {
            try {
                HairstyleRepository.releaseHairStyle(params, pics).enqueue(object : retrofit2.Callback<AddHairStyleResponse> {
                    override fun onResponse(
                        call: Call<AddHairStyleResponse>,
                        response: retrofit2.Response<AddHairStyleResponse>
                    ) {
                        println("addHairStyle 111111")
                        if (response.isSuccessful && response.body() != null) {
                            println("addHairStyle 222222")
                            println(response.body())
//                            if(response.body()?.code != 1){
//                                println("addHairStyle 33333")
//                                _uploadStatus.postValue(Resource.Error("Error: ${response.body()?.code} ${response.body()?.message}"))
//                            }else {
                                println("addHairStyle 4444444")
                                println(response.body())
                                _uploadStatus.postValue(Resource.Success(response.body()!!)) // 使用postValue，因为这可能在非主线程调用
//                            }
                        }
                    }

                    override fun onFailure(call: Call<AddHairStyleResponse>, t: Throwable) {
                        // 网络请求失败
                        _uploadStatus.postValue(Resource.Error("Network error: ${t.localizedMessage}"))
                    }
                })
            } catch (e: Exception) {
                _uploadStatus.value = Resource.Error(e.localizedMessage ?: "An error occurred", null)
            }
        }
    }
}