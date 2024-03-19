package com.sou.hairstyle.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sou.hairstyle.R
import com.sou.hairstyle.databinding.FragmentHomeBinding
import com.sou.hairstyle.ui.hairstyleInfo.HairstyleInfoActivity


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val tags = arrayListOf("超美", "初春", "春夏", "新风", "初秋", "秋冬")

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

        _binding?.searchBar?.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                if (hasFocus){
                    initTags()
                    _binding?.searchOption?.visibility = View.VISIBLE
                } else {
                    _binding?.searchOption?.visibility = View.GONE
                }
            }
        _binding?.searchBar?.setOnClickListener {
            initTags()
            _binding?.searchOption?.visibility = View.VISIBLE
        }
        _binding?.searchOption?.setOnClickListener {
            _binding?.searchOption?.visibility = View.GONE
            search()
        }
        _binding?.searchBar?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                _binding?.searchOption?.visibility = View.GONE
                search()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
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

        homeViewModel.fetchHairstyles(1,50)
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }



    private fun initTags(){
        _binding?.searchBar?.setText("")
        _binding?.tagFlowLayout?.removeAllViews()
        tags.forEach {
            val checkBox = LayoutInflater.from(context).inflate(
                R.layout.add_hair_style_tag_select,
                _binding?.tagFlowLayout,
                false
            ) as CheckBox
            checkBox.text = it
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    _binding?.searchBar?.setText(it)
                    _binding?.searchBar?.setSelection(it.length)
                    search()
                    _binding?.searchOption?.visibility = View.GONE
                }
            }
            _binding?.tagFlowLayout?.addView(checkBox)
        }
    }

    private fun search(){
        _binding?.searchBar?.let { hideSoftInputFromWindow(it) }
        val tag = _binding?.searchBar?.text?.trim().toString()
        if (!tag.isNullOrEmpty()) {
            homeViewModel.searchHairstyles(tag, 1, 20)
        } else {
            homeViewModel.fetchHairstyles(1,50)
        }
    }



    private fun showSoftInputFromWindow(view: View?) {
        if (view == null) return
        view.requestFocus()
        val imm: InputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    private fun hideSoftInputFromWindow(view: View?) {
        if (view == null) return
        view.clearFocus()
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver) }
    }
}