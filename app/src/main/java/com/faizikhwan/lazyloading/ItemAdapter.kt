package com.faizikhwan.lazyloading

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizikhwan.lazyloading.model.Item
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.item_recycler_view.view.*


internal class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val progressBar = itemView.progressBar
}

internal class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val tvName = itemView.tv_name
    val tvLength = itemView.tv_length
}

class ItemAdapter(recyclerView: RecyclerView, internal var activity: Activity, internal var items: MutableList<Item?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_ITEM_TYPE = 0
    val VIEW_LOADING_TYPE = 1

    internal var loadMore: OnLoadMore? = null
    internal var isLoading: Boolean = false
    internal var visibleThreshold = 5
    internal var lastVisibleItem = 0
    internal var totalItemCount = 0

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null) {
                        loadMore?.onLoadMore()
                    }
                    isLoading = true
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_ITEM_TYPE) {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_recycler_view, parent, false)
            return ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false)
            return ItemViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = items[position]
            item?.let {
                holder.tvName.text = item.name
                holder.tvLength.text = "${item.length}"
            }
        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] == null) {
            return VIEW_LOADING_TYPE
        } else {
            return VIEW_ITEM_TYPE
        }
    }

    fun setLoaded() {
        isLoading = false
    }

    fun setLoadMore(onLoadMore: OnLoadMore) {
        this.loadMore = onLoadMore
    }

}