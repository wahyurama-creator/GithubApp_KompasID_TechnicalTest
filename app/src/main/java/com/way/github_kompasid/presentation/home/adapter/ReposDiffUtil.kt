package com.way.github_kompasid.presentation.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.way.github_kompasid.data.remote.response.repos.ReposItem

class ReposDiffUtil(
    private val oldList: List<ReposItem>,
    private val newList: List<ReposItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}