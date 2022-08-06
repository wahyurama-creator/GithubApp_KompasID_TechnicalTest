package com.way.github_kompasid.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.way.github_kompasid.data.remote.network.NetworkResult
import com.way.github_kompasid.databinding.FragmentHomeBinding
import com.way.github_kompasid.presentation.MainActivity
import com.way.github_kompasid.presentation.home.adapter.ItemUserAdapter
import com.way.github_kompasid.presentation.util.observeOnce
import com.way.github_kompasid.presentation.viewmodel.MainViewModel
import com.way.github_kompasid.presentation.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var userAdapter: ItemUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = (activity as MainActivity).factory
        mainViewModel = (activity as MainActivity).mainViewModel
        userAdapter = (activity as MainActivity).userAdapter

        lifecycleScope.launch {
            readFromDatabase()
        }

        setupRecyclerView()
        setupSearchView()
    }

    private fun readFromDatabase() {
        lifecycleScope.launch {
            mainViewModel.readUser.observeOnce(viewLifecycleOwner) { db ->
                if (db.isNotEmpty()) {
                    userAdapter.setRandomUser(db[0].randomUser)
                    showLoading(false)
                } else {
                    randomUserApi()
                }
            }
        }
    }

    private fun loadOfflineCache() {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.readUser.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.d(HomeFragment::class.simpleName, "Get Data From Db")
                    userAdapter.setRandomUser(database[0].randomUser)
                    showLoading(false)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchUserApi(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })
    }

    private fun randomUserApi() {
        showLoading(true)
        Log.d(HomeFragment::class.simpleName, "Request to search called")
        mainViewModel.getRandomUser()
        mainViewModel.randomUserResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showLoading(false)
                    response.data?.let {
                        userAdapter.setRandomUser(it)
                    }
                }
                is NetworkResult.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    loadOfflineCache()
                }
                is NetworkResult.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun searchUserApi(searchQuery: String) {
        showLoading(true)
        Log.d(HomeFragment::class.simpleName, "Request to search called")
        mainViewModel.getSearchUser(searchQuery)
        mainViewModel.searchUserResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showLoading(false)
                    response.data?.let {
                        userAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    loadOfflineCache()
                }
                is NetworkResult.Loading -> {
                    showLoading(true)
                }
            }
        }
    }


    private fun showLoading(isShow: Boolean) {
        if (isShow) {
            binding.progressBar.visibility = VISIBLE
        } else {
            binding.progressBar.visibility = INVISIBLE
        }
    }
}