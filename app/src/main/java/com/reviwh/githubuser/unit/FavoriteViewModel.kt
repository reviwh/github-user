package com.reviwh.githubuser.unit

import androidx.lifecycle.ViewModel
import com.reviwh.githubuser.data.UserRepository

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUsers() = userRepository.getFavoriteUsers()
}