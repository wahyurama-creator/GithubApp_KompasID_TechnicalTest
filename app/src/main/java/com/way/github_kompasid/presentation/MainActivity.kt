package com.way.github_kompasid.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.way.github_kompasid.R
import com.way.github_kompasid.presentation.home.adapter.ItemReposAdapter
import com.way.github_kompasid.presentation.home.adapter.ItemUserAdapter
import com.way.github_kompasid.presentation.viewmodel.MainViewModel
import com.way.github_kompasid.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var userAdapter: ItemUserAdapter
    @Inject
    lateinit var reposAdapter: ItemReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
}