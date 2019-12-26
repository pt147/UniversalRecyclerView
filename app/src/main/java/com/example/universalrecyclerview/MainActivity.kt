package com.example.universalrecyclerview

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.universal_recyclerview.Pagination
import com.example.universal_recyclerview.PullToRefresh
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = ArrayList<String>()


        for (i in 0..20)
            list.add("Hi i am at --> ")

        Handler().postDelayed({
            list.clear()
            recycler_view.setRecyclerViewAdapter(Radapter(this, list))
        }, 5000)


        recycler_view.setOnPagination(object : Pagination {
            override fun OnLoadMore() {

                Handler().postDelayed({
                    for (i in 0..20)
                        list.add("Hi i am at --> ")

                    //Data has been loaded
                    recycler_view.loadComplete()

                    //There is no more data in pagination.
                    recycler_view.noMoreDataInPagination()
                }, 5000)

            }
        })


        recycler_view.setOnPullToRefresh(object : PullToRefresh {
            override fun onPageRefresh() {

                Handler().postDelayed({
                    list.clear()
                    for (i in 0..20)
                        list.add("Hi i am at --> ")

                    //data has been loaded.
                    recycler_view.loadComplete()
                }, 5000)
            }
        })
    }


}
