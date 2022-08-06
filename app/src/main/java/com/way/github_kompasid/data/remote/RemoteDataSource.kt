package com.way.github_kompasid.data.remote

import com.way.github_kompasid.data.remote.network.GithubApi
import com.way.github_kompasid.data.remote.response.user.DetailUser
import com.way.github_kompasid.data.remote.response.user.Search
import com.way.github_kompasid.data.remote.response.repos.Repos
import com.way.github_kompasid.data.remote.response.user.RandomUser
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val githubApi: GithubApi
) {
    suspend fun getRandomUser(): Response<RandomUser> =
        githubApi.getRandomUser()

    suspend fun getSearchUser(searchQuery: String): Response<Search> =
        githubApi.getSearchUser(searchQuery)

    suspend fun getDetailUser(username: String): Response<DetailUser> =
        githubApi.getDetailUser(username)

    suspend fun getReposUser(username: String): Response<Repos> =
        githubApi.getReposUser(username)
}