package com.way.github_kompasid.di

import android.content.Context
import androidx.room.Room
import com.way.github_kompasid.data.local.room.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()

    @Provides
    @Singleton
    fun provideDao(database: UserDatabase) = database.userDao()
}