package com.way.github_kompasid.di

import android.app.Application
import com.way.github_kompasid.data.GithubRepository
import com.way.github_kompasid.presentation.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FactoryModule {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideViewModelFactory(
        repository: GithubRepository,
        app: Application
    ): ViewModelFactory = ViewModelFactory(
        repository, app
    )
}