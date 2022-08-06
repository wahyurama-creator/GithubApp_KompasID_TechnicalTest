package com.way.github_kompasid.presentation.home.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.way.github_kompasid.R
import com.way.github_kompasid.data.remote.response.user.Item
import com.way.github_kompasid.data.remote.response.user.RandomUser
import com.way.github_kompasid.data.remote.response.user.Search
import com.way.github_kompasid.databinding.ItemUserBinding
import javax.inject.Inject

class ItemUserAdapter @Inject constructor() : RecyclerView.Adapter<ItemUserAdapter.ViewHolder>() {

    private var oldUser = emptyList<Item>()

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Item) {
            binding.ivItemUser.load(user.avatarUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_error_placeholder)
                error(R.drawable.ic_error_placeholder)
                transformations(CircleCropTransformation())
            }
            binding.tvUsername.text = user.login
            binding.root.setOnClickListener {
                val bundle = Bundle().apply { putString("username", user.login) }
                Navigation.findNavController(it)
                    .navigate(R.id.action_homeFragment_to_detailFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(oldUser[position])
    }

    override fun getItemCount(): Int = oldUser.size

    fun setData(newUser: Search) {
        val diffUtil = UserDiffUtill(oldUser, newUser.items)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldUser = newUser.items
        diffResults.dispatchUpdatesTo(this)
    }

    fun setRandomUser(user: RandomUser) {
        val diffUtil = UserDiffUtill(oldUser, user)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldUser = user
        diffResults.dispatchUpdatesTo(this)
    }
}