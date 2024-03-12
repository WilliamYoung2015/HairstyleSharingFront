package com.sou.hairstyle.ui.hairstyleInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sou.hairstyle.common.ImageLoader
import com.sou.hairstyle.databinding.CommentItemBinding

class HairstyleInfoAdapter : ListAdapter<CommentItem, HairstyleInfoAdapter.CommentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentViewHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentItem) {
            binding.apply {
                // Use Glide or another image loading library to load the user profile image
                textViewNickname.text = comment.username
                textViewComment.text = comment.content
                // imageViewProfile should be loaded with comment.userProfileImageUrl
                imageViewProfile.setImageDrawable(null)

                ImageLoader.loadImage(imageViewProfile.context, comment.userProfileImageUrl, imageViewProfile)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CommentItem>() {
        override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem.username == newItem.username // Assuming username is unique
        }

        override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem == newItem
        }
    }
}