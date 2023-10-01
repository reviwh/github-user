package com.reviwh.githubuser.di

import android.content.Context
import com.reviwh.githubuser.data.UserRepository
import com.reviwh.githubuser.data.local.room.UserDatabase

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = UserDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        return UserRepository.getInstance(dao)
    }
}