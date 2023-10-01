package com.reviwh.githubuser.unit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.reviwh.githubuser.data.remote.response.GithubResponse
import com.reviwh.githubuser.data.remote.response.ItemsItem
import com.reviwh.githubuser.data.remote.retrofit.ApiConfig
import com.reviwh.githubuser.utils.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _user = MutableLiveData<List<ItemsItem>>()
    val user: LiveData<List<ItemsItem>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFound = MutableLiveData<Boolean>()
    val isFound: LiveData<Boolean> = _isFound

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getAllUser()
    }

    fun findUser(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().findUser(q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>, response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()?.items?.filterNotNull()
                    _isFound.value = response.body()?.items?.isNotEmpty()
                } else {
                    _errorMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "onFailure: ${t.message}"
            }
        })
    }

    fun getAllUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllUser()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                    _isFound.value = response.body()?.isNotEmpty()
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

    fun getThemeSetting(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()
    fun saveThemeSetting(isDarkMode: Boolean){
        viewModelScope.launch { pref.saveThemeSetting(isDarkMode) }
    }
}