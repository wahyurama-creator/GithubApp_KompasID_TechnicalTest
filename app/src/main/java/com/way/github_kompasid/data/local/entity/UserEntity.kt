package com.way.github_kompasid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.way.github_kompasid.data.remote.response.user.RandomUser

@Entity(tableName = "user_table")
class UserEntity(
    var randomUser: RandomUser
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}