package com.reviwh.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reviwh.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Query("DELETE FROM userentity WHERE username = :username")
    suspend fun delete(username: String)

    @Query("SELECT * FROM userentity")
    fun getFavoriteUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM userentity WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<UserEntity>
}