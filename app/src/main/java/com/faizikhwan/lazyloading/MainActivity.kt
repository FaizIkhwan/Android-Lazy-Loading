package com.faizikhwan.lazyloading

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizikhwan.lazyloading.model.Item
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnLoadMore {

    var items: MutableList<Item?> = ArrayList()
    lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InitRandomData()
        setupRecyclerView()
    }

    override fun onLoadMore() {
        if (items.size < 50) {
            items.add(null)
            adapter.notifyItemInserted(items.size - 1)

            Handler().postDelayed({
                items.removeAt(items.size - 1)
                adapter.notifyItemRemoved(items.size)

                val index = items.size
                val end = index + 10

                for (i in index until end) {
                    val name = UUID.randomUUID().toString()
                    val item = Item(name, name.length)
                    items.add(item)
                }

                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }, 3000)
        } else {
            Toast.makeText(this, "MAX DATA IS 50", Toast.LENGTH_SHORT).show()
        }
    }

    fun InitRandomData() {
        for (i in 0..9) {
            val name = UUID.randomUUID().toString()
            val item = Item(name, name.length)
            items.add(item)
        }
    }

    fun setupRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(recycler_view, this, items)
        recycler_view.adapter = adapter
        adapter.setLoadMore(this)
    }
}
