package com.sou.hairstyle.ui.hairstylelist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sou.hairstyle.R
import com.sou.hairstyle.common.PublicData
import com.sou.hairstyle.databinding.ActivityHairstyleListBinding
import com.sou.hairstyle.ui.hairstyleInfo.HairstyleInfoActivity
import com.sou.hairstyle.ui.home.HairInfoAdapter
import com.sou.hairstyle.ui.home.HomeViewModel
import com.sou.hairstyle.widget.LoadMoreLinearLayoutRecyclerView

class HairStyleListActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHairstyleListBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var hairInfoAdapter: HairInfoAdapter


    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == PublicData.HAIRSTYLE_INFO_REQUEST_CODE) {
                // 这里执行刷新Fragment的操作
                val styleId = intent.getStringExtra("styleId")
                if (!styleId.isNullOrEmpty()) {
                    styleId.toIntOrNull()?.let {
                        hairInfoAdapter.notifyById(styleId, PublicData.getFavoriteStatus(it))
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityHairstyleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        val from = intent.getStringExtra("from")?: "favorite"
        setTitle(if (from == "release") R.string.me_release else R.string.me_favorite)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
            IntentFilter(PublicData.HAIRSTYLE_INFO_REQUEST_CODE)
        )
        hairInfoAdapter = HairInfoAdapter(arrayListOf(), homeViewModel, false){ item ->
            val gson = Gson()
            val itemJson = gson.toJson(item)
            println(itemJson)
            val intent = Intent(this, HairstyleInfoActivity::class.java).apply {
                putExtra("HAIR_INFO_ITEM_JSON", itemJson) // Pass the JSON string
            }
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hairInfoAdapter
            addLoadMoreListener {
                binding.recyclerView.setLoadStatus(LoadMoreLinearLayoutRecyclerView.LOADING)
                homeViewModel.loadMoreData("", from)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            hairInfoAdapter.clearData()
            getListData(from)
        }
        if (from == "release") {
            homeViewModel.hairInfoRelease.observe(this) { items ->
                binding.swipeRefreshLayout.isRefreshing = false
                if (items.isNotEmpty()) {
                    binding.recyclerView.setLoadStatus(LoadMoreLinearLayoutRecyclerView.LOAD_COMPLETE)
                    hairInfoAdapter.addMoreData(items)
                } else {
                    binding.recyclerView.setLoadStatus(LoadMoreLinearLayoutRecyclerView.NO_MORE_DATA)
                }
                if (items.size < 10) {
                    hairInfoAdapter.setShowFooter(true)
                }
            }
            homeViewModel.hairInfoFavorite.observe(this) {
                //2.再拉取发型列表，同步收藏状态
                homeViewModel.getReleaseListData(1, 20)
            }
            //1.先获取收藏列表
            homeViewModel.getFavoriteListData()
        } else {
            homeViewModel.hairInfoFavorite.observe(this) { items ->
                binding.swipeRefreshLayout.isRefreshing = false
                if (items.isNotEmpty()) {
                    binding.recyclerView.setLoadStatus(LoadMoreLinearLayoutRecyclerView.LOAD_COMPLETE)
                    hairInfoAdapter.addMoreData(items)
                } else {
                    binding.recyclerView.setLoadStatus(LoadMoreLinearLayoutRecyclerView.NO_MORE_DATA)
                }
                if (items.size < 10) {
                    hairInfoAdapter.setShowFooter(true)
                }
            }
            homeViewModel.getFavoriteListData(1, 20)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 当点击返回按钮时，结束当前活动
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getListData(from: String?){
        if (from == "release"){
            homeViewModel.getReleaseListData(1, 20)
        } else {
            homeViewModel.getFavoriteListData(1, 20)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
            homeViewModel.hairInfoFavorite.removeObservers(this)
        }
    }
}