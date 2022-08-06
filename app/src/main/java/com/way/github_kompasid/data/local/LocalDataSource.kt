package com.way.github_kompasid.data.local

import com.way.github_kompasid.data.local.entity.UserEntity
import com.way.github_kompasid.data.local.room.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insertUser(userEntity: UserEntity) =
        userDao.insertUser(userEntity)

    fun readUser(): Flow<List<UserEntity>> =
        userDao.readUser()
}