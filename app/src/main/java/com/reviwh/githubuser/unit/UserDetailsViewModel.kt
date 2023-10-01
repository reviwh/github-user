package com.reviwh.githubuser.unit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reviwh.githubuser.data.UserRepository
import com.reviwh.githubuser.data.local.entity.UserEntity
import com.reviwh.githubuser.data.remote.response.UserDetailResponse
import com.reviwh.githubuser.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userDetailResponse = MutableLiveData<UserDetailResponse>()
    val userDetailResponse: LiveData<UserDetailResponse> = _userDetailResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getUserData(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetailResponse.value = response.body()
                } else {
                    _errorMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "onFailure: ${t.message}"
            }
        })
    }

    fun addUserToFavorite(userEntity: UserEntity) = viewModelScope.launch { userRepository.insert(userEntity) }
    fun removeUserFromFavorite(username: String) = viewModelScope.launch { userRepository.delete(username) }

    fun getFavoriteUserByUsername(username: String) =
        userRepository.getFavoriteUserByUsername(username)
}