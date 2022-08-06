package com.way.github_kompasid.data

import com.way.github_kompasid.data.local.LocalDataSource
import com.way.github_kompasid.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class GithubRepository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource
)