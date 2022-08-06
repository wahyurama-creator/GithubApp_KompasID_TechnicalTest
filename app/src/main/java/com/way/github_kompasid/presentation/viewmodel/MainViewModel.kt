package com.way.github_kompasid.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.way.github_kompasid.data.GithubRepository
import com.way.github_kompasid.data.local.entity.UserEntity
import com.way.github_kompasid.data.remote.network.NetworkResult
import com.way.github_kompasid.data.remote.response.repos.Repos
import com.way.github_kompasid.data.remote.response.user.DetailUser
import com.way.github_kompasid.data.remote.response.user.RandomUser
import com.way.github_kompasid.data.remote.response.user.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
    private val app: Application
) : AndroidViewModel(app) {

    // Local Data Source
    var readUser: LiveData<List<UserEntity>> =
        githubRepository.localDataSource.readUser().asLiveData()

    private fun insertUser(userEntity: UserEntity) = viewModelScope.launch(Dispatchers.IO) {
        githubRepository.localDataSource.insertUser(userEntity)
    }

    private fun offlineCacheUser(user: RandomUser) {
        val userEntity = UserEntity(user)
        insertUser(userEntity)
    }

    // Remote Data Source
    var randomUserResponse: MutableLiveData<NetworkResult<RandomUser>> = MutableLiveData()
    var searchUserResponse: MutableLiveData<NetworkResult<Search>> = MutableLiveData()
    var detailUserResponse: MutableLiveData<NetworkResult<DetailUser>> = MutableLiveData()
    var reposUserResponse: MutableLiveData<NetworkResult<Repos>> = MutableLiveData()

    fun getRandomUser() = viewModelScope.launch {
        getRandomUserSafeCall()
    }

    fun getSearchUser(searchQuery: String) = viewModelScope.launch {
        getSearchUserSafeCall(searchQuery)
    }

    fun getDetailUser(username: String) = viewModelScope.launch {
        getDetailUserSafeCall(username)
    }

    fun getReposUser(username: String) = viewModelScope.launch {
        getReposUserSafeCall(username)
    }

    private suspend fun getReposUserSafeCall(username: String) {
        reposUserResponse.value = NetworkResult.Loading()
        if (isInternetAvailable()) {
            try {
                val response = githubRepository.remoteDataSource.getReposUser(username)
                reposUserResponse.value = handleReposResponse(response)
            } catch (e: Exception) {
                reposUserResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            reposUserResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getSearchUserSafeCall(searchQuery: String) {
        searchUserResponse.value = NetworkResult.Loading()
        if (isInternetAvailable()) {
            try {
                val response = githubRepository.remoteDataSource.getSearchUser(searchQuery)
                searchUserResponse.value = handleSearchResponse(response)
            } catch (e: Exception) {
                searchUserResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            searchUserResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getDetailUserSafeCall(username: String) {
        detailUserResponse.value = NetworkResult.Loading()
        if (isInternetAvailable()) {
            try {
                val response = githubRepository.remoteDataSource.getDetailUser(username)
                detailUserResponse.value = handleDetailUserResponse(response)
            } catch (e: Exception) {
                detailUserResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            detailUserResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getRandomUserSafeCall() {
        randomUserResponse.value = NetworkResult.Loading()
        if (isInternetAvailable()) {
            try {
                val response = githubRepository.remoteDataSource.getRandomUser()
                randomUserResponse.value = handleRandomUserResponse(response)

                // Save to local
                val randomUser = randomUserResponse.value?.data
                if (randomUser != null) {
                    offlineCacheUser(randomUser)
                }
            } catch (e: Exception) {
                randomUserResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            randomUserResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleSearchResponse(response: Response<Search>): NetworkResult<Search> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Error Connection Timeout")
            }
            response.code() == 401 -> {
                return NetworkResult.Error("Error Require Authentication")
            }
            response.code() == 403 -> {
                return NetworkResult.Error("Error Forbidden")
            }
            response.code() == 404 -> {
                return NetworkResult.Error("Error Resource Not Found")
            }
            response.body()!!.items.isEmpty() -> {
                return NetworkResult.Error("Error Users Not Found")
            }
            response.isSuccessful -> {
                val users = response.body()
                return if (users != null) {
                    NetworkResult.Success(users)
                } else {
                    NetworkResult.Error(response.message())
                }
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleDetailUserResponse(response: Response<DetailUser>): NetworkResult<DetailUser> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Error Connection Timeout")
            }
            response.code() == 401 -> {
                return NetworkResult.Error("Error Require Authentication")
            }
            response.code() == 403 -> {
                return NetworkResult.Error("Error Forbidden")
            }
            response.code() == 404 -> {
                return NetworkResult.Error("Error Resource Not Found")
            }
            response.isSuccessful -> {
                val users = response.body()
                return if (users != null) {
                    NetworkResult.Success(users)
                } else {
                    NetworkResult.Error(response.message())
                }
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleReposResponse(response: Response<Repos>): NetworkResult<Repos> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Error Connection Timeout")
            }
            response.code() == 401 -> {
                return NetworkResult.Error("Error Require Authentication")
            }
            response.code() == 403 -> {
                return NetworkResult.Error("Error Forbidden")
            }
            response.code() == 404 -> {
                return NetworkResult.Error("Error Resource Not Found")
            }
            response.isSuccessful -> {
                val users = response.body()
                return if (users != null) {
                    NetworkResult.Success(users)
                } else {
                    NetworkResult.Error(response.message())
                }
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleRandomUserResponse(response: Response<RandomUser>): NetworkResult<RandomUser> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Error Connection Timeout")
            }
            response.code() == 401 -> {
                return NetworkResult.Error("Error Require Authentication")
            }
            response.code() == 403 -> {
                return NetworkResult.Error("Error Forbidden")
            }
            response.code() == 404 -> {
                return NetworkResult.Error("Error Resource Not Found")
            }
            response.isSuccessful -> {
                val users = response.body()
                return if (users != null) {
                    NetworkResult.Success(users)
                } else {
                    NetworkResult.Error(response.message())
                }
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }
}