package com.reviwh.githubuser.unit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reviwh.githubuser.data.remote.response.ItemsItem
import com.reviwh.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _followResponseItem = MutableLiveData<List<ItemsItem>>()
    val followResponseItem: LiveData<List<ItemsItem>> = _followResponseItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _hasFollower = MutableLiveData<Boolean>()
    val hasFollower: LiveData<Boolean> = _hasFollower

    private val _hasFollowing = MutableLiveData<Boolean>()
    val hasFollowing: LiveData<Boolean> = _hasFollowing

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followResponseItem.value = response.body()
                    _hasFollower.value = response.body()?.isNotEmpty()
                } else {
                    _errorMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "onFailure: ${t.message}"
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followResponseItem.value = response.body()
                    _hasFollowing.value = response.body()?.isNotEmpty()
                } else {
                    _errorMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "onFailure: ${t.message}"
            }
        })
    }
}