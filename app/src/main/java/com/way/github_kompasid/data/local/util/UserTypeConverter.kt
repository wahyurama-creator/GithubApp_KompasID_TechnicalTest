package com.way.github_kompasid.data.local.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.way.github_kompasid.data.remote.response.user.RandomUser

class UserTypeConverter {
    private lateinit var gson: Gson

    @TypeConverter
    fun userToString(randomUser: RandomUser): String {
        gson = Gson()
        return gson.toJson(randomUser)
    }

    @TypeConverter
    fun stringToUser(data: String): RandomUser {
        gson = Gson()
        val listOfType = object : TypeToken<RandomUser>() {}.type
        return gson.fromJson(data, listOfType)
    }
}