package com.way.github_kompasid.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.way.github_kompasid.data.remote.network.NetworkResult
import com.way.github_kompasid.data.remote.response.user.DetailUser
import com.way.github_kompasid.databinding.FragmentDetailBinding
import com.way.github_kompasid.presentation.MainActivity
import com.way.github_kompasid.presentation.home.adapter.ItemReposAdapter
import com.way.github_kompasid.presentation.viewmodel.MainViewModel
import com.way.github_kompasid.presentation.viewmodel.ViewModelFactory

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var usernameArgs: String
    private lateinit var mainViewModel: MainViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var reposAdapter: ItemReposAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameArgs = arguments?.getString("username") as String
        factory = (activity as MainActivity).factory
        mainViewModel = (activity as MainActivity).mainViewModel
        reposAdapter = (activity as MainActivity).reposAdapter

        getDetailUser()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvRepos.apply {
            adapter = reposAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getDetailUser() {
        isShowLoading(true)
        mainViewModel.getDetailUser(usernameArgs)
        mainViewModel.detailUserResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    isShowLoading(false)
                    response.data?.let {
                        applyData(it)
                    }
                    getReposUser()
                }
                is NetworkResult.Error -> {
                    isShowLoading(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    isShowLoading(true)
                }
            }
        }
    }

    private fun getReposUser() {
        isShowLoading(true)
        mainViewModel.getReposUser(usernameArgs)
        mainViewModel.reposUserResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    isShowLoading(false)
                    response.data?.let {
                        reposAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    isShowLoading(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    isShowLoading(true)
                }
            }
        }
    }

    private fun applyData(user: DetailUser) {
        binding.ivUser.load(user.avatarUrl) {
            transformations(CircleCropTransformation())
        }
        binding.tvUsernameDetail.text = user.name ?: "Unavailable"
        binding.tvUsernameLogin.text = ("@" + user.username) ?: "Unavailable"
        binding.tvDesc.text = user.bio ?: "Unavailable"
    }

    private fun isShowLoading(isShow: Boolean) {
        with(binding) {
            if (isShow) progressBar.visibility = VISIBLE
            else progressBar.visibility = INVISIBLE
        }
    }
}