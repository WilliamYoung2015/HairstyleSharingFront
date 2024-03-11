package com.sou.hairstyle.ui.hairstyleInfo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sou.hairstyle.R
import com.sou.hairstyle.common.ImageLoader
import com.sou.hairstyle.databinding.ActivityHairstyleInfoBinding
import com.sou.hairstyle.ui.home.HairInfoItem
import com.sou.hairstyle.ui.home.HomeFragment

class HairstyleInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHairstyleInfoBinding
    private val viewModel: HairstyleInfoViewModel by viewModels()
    private lateinit var adapter: HairstyleInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHairstyleInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.hair_style_detail);

        val itemJson = intent.getStringExtra("HAIR_INFO_ITEM_JSON")
        val item = Gson().fromJson(itemJson, HairInfoItem::class.java)

        ImageLoader.loadImage(this, item.imageUrl, binding.imgLarge)

        binding.txtCommentCount.text = getString(R.string.comment_count_format, item.comment_count)
        binding.txtLikeCount.text = item.star_sum.toString()
        setHeartIcon(item.isLiked)

        setupRecyclerView()
        viewModel.comments.observe(this) { comments ->
            adapter.submitList(comments)
        }

        binding.imgHeart.setOnClickListener {
            item.isLiked = !item.isLiked
            setHeartIcon(item.isLiked)
            // Update the like count accordingly
            item.star_sum += if (item.isLiked) 1 else -1
            binding.txtLikeCount.text = item.star_sum.toString()
            // TODO: Update the like status on the server or local database
        }

        binding.buttonAddComment.setOnClickListener {
            // Handle add comment click
        }
    }

    private fun setHeartIcon(isLiked: Boolean) {
        binding.imgHeart.setImageResource(if (isLiked) R.drawable.heart_solid else R.drawable.heart_outline)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(HomeFragment.HAIRSTYLE_INFO_REQUEST_CODE))
        finish()
    }

    private fun setupRecyclerView() {
        adapter = HairstyleInfoAdapter()
        binding.recyclerViewComments.apply {
            layoutManager = LinearLayoutManager(this@HairstyleInfoActivity)
            adapter = this@HairstyleInfoActivity.adapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 当点击返回按钮时，结束当前活动
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}