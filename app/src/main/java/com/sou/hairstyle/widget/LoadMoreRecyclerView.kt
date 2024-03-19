package com.sou.hairstyle.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreLinearLayoutRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    companion object {
        val LOADING = 1
        val LOAD_COMPLETE = 2
        val NO_MORE_DATA = 3
    }
    private var loadMoreCallback: (()->Unit)? = null
    // 当前展示的最后一项+阈值，即即将进入加载区域的边界
    private var visibleThreshold = 3
    private var loadStatus = LOAD_COMPLETE
    init {
        addOnScrollListener(object : OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager is LinearLayoutManager) {
                    val totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                    if (totalItemCount <= 0) return
                    val lastVisibleItem =
                        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (lastVisibleItem <= 0) return
                    if (loadStatus == LOAD_COMPLETE && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // 如果不是已经在加载，并且滚动到了预定的阈值之内，则开始加载新数据
                        loadMoreCallback?.invoke()
                    }
                }
            }
        })
    }

    fun addLoadMoreListener(loadMore: ()-> Unit) {
        loadMoreCallback = loadMore
    }

    fun setLoadStatus(status: Int){
        this.loadStatus = status
    }
}