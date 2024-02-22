package com.sou.hairstyle.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sou.hairstyle.R

class HairInfoAdapter(var items: List<HairInfoItem>) : RecyclerView.Adapter<HairInfoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val avatarView: ImageView = view.findViewById(R.id.item_avatar)
        val titleView: TextView = view.findViewById(R.id.item_title)
        val descriptionView: TextView = view.findViewById(R.id.item_description)
        val likeCountView: TextView = view.findViewById(R.id.item_like_count)
        val likeButton: ImageView = view.findViewById(R.id.item_like_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hair_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Use Glide or another library to load images
        Glide.with(holder.imageView.context)
            .load(item.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_delete)
            .into(holder.imageView)

        Glide.with(holder.avatarView.context)
            .load(item.avatarUrl)
            .circleCrop() // if you want to crop the image in a circle
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_delete)
            .into(holder.avatarView)

        holder.titleView.text = item.title
        holder.descriptionView.text = item.description
        holder.likeCountView.text = item.likeCount.toString()
        holder.likeButton.setImageResource(if (item.isLiked) android.R.drawable.star_big_on else android.R.drawable.star_big_off)


        // Set up a click listener for the like button
        holder.likeButton.setOnClickListener {
            // Update the isLiked status
            item.isLiked = !item.isLiked
            item.likeCount += if (item.isLiked) 1 else -1
            notifyItemChanged(position)
        }

        // You might want to add a click listener to the whole item view as well
        holder.itemView.setOnClickListener {
            // Implement your click handling logic
        }
    }

    override fun getItemCount(): Int = items.size
}