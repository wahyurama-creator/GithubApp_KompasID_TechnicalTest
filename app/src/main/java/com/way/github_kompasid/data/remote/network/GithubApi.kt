package com.way.github_kompasid.data.remote.network

import com.way.github_kompasid.data.remote.response.user.DetailUser
import com.way.github_kompasid.data.remote.response.user.Search
import com.way.github_kompasid.data.remote.response.repos.Repos
import com.way.github_kompasid.data.remote.response.user.RandomUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users")
    suspend fun getRandomUser(): Response<RandomUser>

    @GET("/search/users")
    suspend fun getSearchUser(
        @Query("q")
        searchQuery: String
    ): Response<Search>

    @GET("/users/{username}")
    suspend fun getDetailUser(
        @Path("username")
        username: String
    ): Response<DetailUser>

    @GET("/users/{username}/repos")
    suspend fun getReposUser(
        @Path("username")
        username: String
    ): Response<Repos>
}