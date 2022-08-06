package com.way.github_kompasid.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.way.github_kompasid.data.remote.response.repos.Repos
import com.way.github_kompasid.data.remote.response.repos.ReposItem
import com.way.github_kompasid.databinding.ItemReposBinding
import javax.inject.Inject

class ItemReposAdapter @Inject constructor() : RecyclerView.Adapter<ItemReposAdapter.ViewHolder>() {

    private var oldUser = emptyList<ReposItem>()

    inner class ViewHolder(private val binding: ItemReposBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reposItem: ReposItem) {
            binding.tvTitleRepos.text = reposItem.name
            binding.tvDescRepos.text = reposItem.description
            binding.tvStar.text = reposItem.watchers.toString()
            binding.tvUpdated.text = reposItem.updatedAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(oldUser[position])
    }

    override fun getItemCount(): Int = oldUser.size

    fun setData(repos: Repos) {
        val diffUtil = ReposDiffUtil(oldUser, repos)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldUser = repos
        diffResults.dispatchUpdatesTo(this)
    }
}