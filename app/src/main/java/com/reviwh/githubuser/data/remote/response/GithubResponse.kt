package com.reviwh.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

    @field:SerializedName("total_count") val totalCount: Int? = null,

    @field:SerializedName("incomplete_results") val incompleteResults: Boolean? = null,

    @field:SerializedName("items") val items: List<ItemsItem?>? = null
)

data class ItemsItem(

    @field:SerializedName("login") val login: String? = null,

    @field:SerializedName("avatar_url") val avatarUrl: String? = null,


    )
