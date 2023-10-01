package com.reviwh.githubuser.data

import androidx.lifecycle.LiveData
import com.reviwh.githubuser.data.local.entity.UserEntity
import com.reviwh.githubuser.data.local.room.UserDao

class UserRepository(
    private val userDao: UserDao
) {
    fun getFavoriteUsers(): LiveData<List<UserEntity>> = userDao.getFavoriteUsers()
    suspend fun insert(userEntity: UserEntity) = userDao.insert(userEntity)
    suspend fun delete(username: String) = userDao.delete(username)

    fun getFavoriteUserByUsername(username: String): LiveData<UserEntity> =
        userDao.getFavoriteUserByUsername(username)

    companion object {
        private var instance: UserRepository? = null
        fun getInstance(userDao: UserDao): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userDao)
        }.also { instance = it }
    }
}