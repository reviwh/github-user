package com.reviwh.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reviwh.githubuser.data.local.entity.UserEntity
import com.reviwh.githubuser.databinding.ItemRowUserBinding
import com.reviwh.githubuser.loadImage

class FavoriteAdapter : ListAdapter<UserEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.apply {
            bind(user)
            itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(holder.adapterPosition)) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userEntity: UserEntity) {
            binding.apply {
                tvUsername.text = userEntity.username
                imgAvatar.loadImage(userEntity.avatarUrl)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}