package com.way.github_kompasid.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.way.github_kompasid.data.local.entity.UserEntity
import com.way.github_kompasid.data.local.util.UserTypeConverter

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(UserTypeConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}