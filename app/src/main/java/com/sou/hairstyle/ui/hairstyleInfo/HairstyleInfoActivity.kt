package com.sou.hairstyle.ui.hairstyleInfo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
    private lateinit var hairStyleId: String
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

        hairStyleId = item.id
        ImageLoader.loadImage(this, item.imageUrl, binding.imgLarge)



        setupRecyclerView()

        viewModel.hairInfo.observe(this) { hairInfo ->
            binding.txtCommentCount.text = getString(R.string.comment_count_format, hairInfo.comment_count)
            binding.txtLikeCount.text = hairInfo.star_sum.toString()
//            setHeartIcon(hairInfo.isLiked)
        }

        viewModel.comments.observe(this) { comments ->
            adapter.submitList(comments)
        }
        viewModel.getCurrentHairStyleInfo(hairStyleId)
        viewModel.fetchComments(1,20,hairStyleId)

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
//            AddCommentDialogFragment().show(supportFragmentManager, "CustomDialogFragment")
            showCustomDialog()
        }
    }

    private fun setHeartIcon(isLiked: Boolean) {
        binding.imgHeart.setImageResource(if (isLiked) R.drawable.stared else R.drawable.star)
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
    fun showCustomDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_comment, null)
        val alertDialog = AlertDialog.Builder(this, R.style.FullScreenDialog)
            .setView(dialogView)
            .create()
        // 设置全屏遮罩层
        alertDialog.window?.apply {
            // 设置背景为全屏半透明遮罩
            setBackgroundDrawableResource(R.drawable.overlay_dim)
            // 布局参数全屏设置
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        // 显示弹出框
        alertDialog.show()

        // 设置点击遮罩层外的区域不关闭弹窗
        alertDialog.setCanceledOnTouchOutside(false)

        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonConfirm = dialogView.findViewById<Button>(R.id.buttonConfirm)

        buttonCancel.setOnClickListener {
            // 处理取消按钮点击事件
            alertDialog.dismiss()
        }

        buttonConfirm.setOnClickListener {
            // 处理确认按钮点击事件
            val editTextComment = dialogView.findViewById<EditText>(R.id.editTextComment)
            val comment = editTextComment.text.toString()
            // TODO: 逻辑处理
            viewModel.addComment(comment, hairStyleId) {
                if (it) {
                    viewModel.getCurrentHairStyleInfo(hairStyleId)
                    viewModel.fetchComments(1, 20, hairStyleId)
                }


                alertDialog.dismiss()
            }
        }
    }
}