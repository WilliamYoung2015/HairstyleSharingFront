package com.sou.hairstyle.ui.hairstyleInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sou.hairstyle.R
import com.sou.hairstyle.common.ImageLoader
import com.sou.hairstyle.databinding.CommentItemBinding

class HairstyleInfoAdapter : ListAdapter<CommentItem, RecyclerView.ViewHolder>(DiffCallback()) {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_FOOTER = 1
    private var showFooter = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CommentViewHolder(binding)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_view, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommentViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return if (showFooter) super.getItemCount() + 1 else super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) VIEW_TYPE_ITEM else VIEW_TYPE_FOOTER
    }


    fun setShowFooter(show: Boolean) {
        showFooter = show
        notifyDataSetChanged() // 通知数据变化，重新渲染列表
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

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