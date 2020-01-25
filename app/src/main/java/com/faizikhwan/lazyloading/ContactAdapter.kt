package com.faizikhwan.lazyloading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faizikhwan.lazyloading.model.Contact
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.item_recycler_view.view.*

class ContactAdapter(private val contacts: ArrayList<Contact?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TIME_ITEM = 0
    private val VIEW_TIME_LOADING = 1
    val VISIBLE_THRESHOLD = 5

    lateinit var callback: OnLoadMoreInterface
    var isLoading = false
    var lastVisibleItem = 0
    var totalItemCount = 0

    interface OnLoadMoreInterface {
        fun onLoadMore()
    }

    private class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmail: TextView = view.tv_email
        val tvPhone: TextView = view.tv_phone
        lateinit var contact: Contact

        fun bind(contact: Contact?) {
            contact?.let {
                this.contact = it
                tvEmail.text = it.email
                tvPhone.text = it.phone
            }
        }
    }

    private class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.progressBar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        if (viewType == VIEW_TIME_ITEM) {
            viewHolder = ContactViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
            )
        } else {
            viewHolder = LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            )
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ContactViewHolder -> {
                holder.bind(contacts[position])
            }
            is LoadingViewHolder -> {
                holder.progressBar.isIndeterminate = true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (contacts[position] == null)
            return VIEW_TIME_LOADING
        else
            return VIEW_TIME_ITEM
    }

    fun setOnLoadMoreListener(onLoadMoreInterface: OnLoadMoreInterface) {
        this.callback = onLoadMoreInterface
    }

    fun setLoaded() {
        isLoading = false
    }

    fun isScrolling(firstVisibleItemPosition: Int) {
        totalItemCount = itemCount
        lastVisibleItem = firstVisibleItemPosition
        if (!isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
            if (callback != null) {
                callback.onLoadMore()
            }
            isLoading = true
        }
    }
}