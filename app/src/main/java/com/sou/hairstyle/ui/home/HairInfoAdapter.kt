package com.sou.hairstyle.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sou.hairstyle.R
import com.sou.hairstyle.common.ImageLoader

class HairInfoAdapter(var items: ArrayList<HairInfoItem>,
                      private val homeViewModel: HomeViewModel,
                      private var showFooter: Boolean,
                      private val onItemClicked: (HairInfoItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_FOOTER = 1

    class ItemViewHolder(view: View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { clickAtPosition(adapterPosition) }
        }
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val avatarView: ImageView = view.findViewById(R.id.item_avatar)
        val titleView: TextView = view.findViewById(R.id.item_title)
        val descriptionView: TextView = view.findViewById(R.id.item_description)
        val likeCountView: TextView = view.findViewById(R.id.item_like_count)
        val likeButton: ImageView = view.findViewById(R.id.item_like_button)
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hair_info, parent, false)
    //            return ItemViewHolder(view)
            ItemViewHolder(view) { position ->
                val item = items[position]
                onItemClicked(item)
            }
        } else { // VIEW_TYPE_FOOTER
            val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_view, parent, false)
            FooterViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ItemViewHolder) {
            val item = items[position]

            // Use Glide or another library to load images
//            Glide.with(holder.imageView.context)
//                .load(item.imageUrl)
//                .placeholder(android.R.drawable.ic_menu_gallery)
//                .error(android.R.drawable.ic_delete)
//                .into(holder.imageView)
            holder.imageView.setImageDrawable(null)
            ImageLoader.loadImage(holder.imageView.context, item.imageUrl, holder.imageView)

//            Glide.with(holder.avatarView.context)
//                .load(item.avatarUrl)
//                .circleCrop() // if you want to crop the image in a circle
//                .placeholder(android.R.drawable.ic_menu_gallery)
//                .error(android.R.drawable.ic_delete)
//                .into(holder.avatarView)
            holder.avatarView.setImageDrawable(null)
            ImageLoader.loadImage(holder.avatarView.context, item.avatarUrl, holder.avatarView)

            holder.titleView.text = item.title
            holder.descriptionView.text = item.description
            holder.likeCountView.text = item.star_sum.toString()
            holder.likeButton.setImageResource(if (item.isLiked) R.drawable.stared else R.drawable.star)


            // Set up a click listener for the like button
            holder.likeButton.setOnClickListener {
                item.id.toIntOrNull()?.let { intId ->
//                    val params = mapOf(Pair("action", if (item.isLiked) "unbind" else "bind"),
//                        Pair("hairstyle_ids", intId)
//                    )
//                    val ids = IntArray(intId)
                    homeViewModel.favorite(if (item.isLiked) "unbind" else "bind", intId) {
                        if (it){
                            // Update the isLiked status
                            item.isLiked = !item.isLiked
                            item.star_sum += if (item.isLiked) 1 else -1
                            notifyItemChanged(position)
                        }
                    }
                }
            }

            // You might want to add a click listener to the whole item view as well
//            holder.itemView.setOnClickListener {
//                // Implement your click handling logic
//                val intent = Intent(activity, HairstyleInfoActivity::class.java)
//                startActivity(intent)
//            }
        }
    }

    fun setShowFooter(show: Boolean) {
        showFooter = show
        notifyDataSetChanged() // 通知数据变化，重新渲染列表
    }

    fun clearData(){
        items.clear()
        setShowFooter(false)
    }
    fun addMoreData(moreData: List<HairInfoItem>){
        val startNotify = items.size
        items.addAll(moreData)
        notifyItemRangeInserted(startNotify, moreData.size)
        notifyItemRangeChanged(startNotify, moreData.size)
    }

    fun notifyById(styleId: String, isLike: Boolean){
        items.forEachIndexed { index, hairInfoItem ->
            if (hairInfoItem.id == styleId){
                hairInfoItem.isLiked = isLike
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }

    override fun getItemCount(): Int {
        return if (showFooter) items.size + 1 else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) VIEW_TYPE_ITEM else VIEW_TYPE_FOOTER
    }
}