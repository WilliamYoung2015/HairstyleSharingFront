package com.sou.hairstyle.ui.home

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sou.hairstyle.databinding.FragmentHomeBinding
import com.sou.hairstyle.ui.hairstyleInfo.HairstyleInfoActivity


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val HAIRSTYLE_INFO_REQUEST_CODE = "HomeFragment_refresh_data"
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == HAIRSTYLE_INFO_REQUEST_CODE) {
                // 这里执行刷新Fragment的操作
                homeViewModel.initData()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver,
                IntentFilter(HAIRSTYLE_INFO_REQUEST_CODE)
            )
        }
        val adapter = HairInfoAdapter(emptyList(), homeViewModel, false){ item ->
            val gson = Gson()
            val itemJson = gson.toJson(item)
            println("click hair style ~~~~~~~~~~~~~~~~")
            println(itemJson)
            val intent = Intent(activity, HairstyleInfoActivity::class.java).apply {
                putExtra("HAIR_INFO_ITEM_JSON", itemJson) // Pass the JSON string
            }
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && dy > 0) { // Check if scrolling down
                        homeViewModel.loadMoreData() // Trigger data load
                    }
                }
            })
        }

        homeViewModel.hasMoreData.observe(viewLifecycleOwner) { hasMore ->
            adapter.setShowFooter(!hasMore)
            adapter.notifyDataSetChanged()
        }

        // Observe the LiveData containing the list of hair info items
        homeViewModel.hairInfoItems.observe(viewLifecycleOwner) { items ->
            // Update the adapter's data when the observed data changes
            adapter.items = items
            adapter.notifyDataSetChanged()
        }

        homeViewModel.fetchHairstyles(1,20)
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver) }
    }
}