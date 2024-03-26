package com.sou.hairstyle.ui.hairstyleInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sou.hairstyle.R
import com.sou.hairstyle.common.ImageLoader
import com.sou.hairstyle.common.PublicData
import com.sou.hairstyle.databinding.CommentItemBinding
import com.sou.hairstyle.ui.home.HairInfoItem

class HairstyleInfoAdapter(val longClickAtPosition: (Int, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_FOOTER = 1
    private var showFooter = false
    private val comments: ArrayList<CommentItem> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CommentViewHolder(binding){ position ->
                val item = comments[position]
                if (item.userId == PublicData.loginUid) {
                    longClickAtPosition.invoke(position, item.id)
                }
            }
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_view, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommentViewHolder) {
            holder.bind(comments[position])
        }
    }

    override fun getItemCount(): Int {
        return if (showFooter) comments.size + 1 else comments.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < comments.size) VIEW_TYPE_ITEM else VIEW_TYPE_FOOTER
    }


    fun setShowFooter(show: Boolean) {
        showFooter = show
        notifyDataSetChanged() // 通知数据变化，重新渲染列表
    }

    fun clearData(){
        comments.clear()
        setShowFooter(false)
    }
    fun addMoreData(moreData: List<CommentItem>){
        val startNotify = comments.size
        comments.addAll(moreData)
        notifyItemRangeInserted(startNotify, moreData.size)
        notifyItemRangeChanged(startNotify, moreData.size)
    }

    fun removeData(position: Int) {
        comments.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, comments.size - position)
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class CommentViewHolder(private val binding: CommentItemBinding, longClickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnLongClickListener {
                longClickAtPosition.invoke(adapterPosition)
                return@setOnLongClickListener true
            }
        }
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
}