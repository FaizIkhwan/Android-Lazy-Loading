package com.faizikhwan.lazyloading

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizikhwan.lazyloading.model.Contact
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var contacts: ArrayList<Contact?> = ArrayList()
    private lateinit var contactAdapter: ContactAdapter
    private var random: Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..500) {
            contacts.add(Contact(phoneNumberGenerating(), "email$i@gmail.com"))
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)

        recycler_view.apply {
            layoutManager = linearLayoutManager
            contactAdapter = ContactAdapter(contacts)
            adapter = contactAdapter
        }

        contactAdapter.setOnLoadMoreListener(object: ContactAdapter.OnLoadMoreInterface {
            override fun onLoadMore() {
                if (contacts.size <= 20) {
                    contacts.add(null)
                    contactAdapter.notifyItemInserted(contacts.size - 1)
                    Handler().postDelayed({
                        contacts.removeAt(contacts.size - 1)
                        contactAdapter.notifyItemRemoved(contacts.size)
                        //Generating more data
                        val index: Int = contacts.size
                        val end = index + 10
                        for (i in index until end) {
                            contacts.add(Contact(phoneNumberGenerating(), "email$i@gmail.com"))
                        }
                        contactAdapter.notifyDataSetChanged()
                        contactAdapter.setLoaded()
                    }, 5000)
                } else {
                    Toast.makeText(this@MainActivity, "Loading data completed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                contactAdapter.isScrolling(linearLayoutManager.findFirstVisibleItemPosition())
            }
        })
    }

    private fun phoneNumberGenerating(): String {
        val low = 100000000
        val high = 999999999
        val randomNumber = random.nextInt(high - low) + low
        return "0$randomNumber"
    }
}
