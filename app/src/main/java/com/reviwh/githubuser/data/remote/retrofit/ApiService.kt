package com.reviwh.githubuser.data.remote.retrofit

import com.reviwh.githubuser.data.remote.response.GithubResponse
import com.reviwh.githubuser.data.remote.response.ItemsItem
import com.reviwh.githubuser.data.remote.response.UserDetailResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun findUser(@Query("q") q: String): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username")   username: String): Call<List<ItemsItem>>

    @GET("users")
    fun getAllUser(): Call<List<ItemsItem>>
}