package com.example.universalrecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_temp_layout.view.*

class Radapter(context: Context, val list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SenderVH(mInflater.inflate(R.layout.row_temp_layout, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SenderVH)
            holder.set(holder.getAdapterPosition())

    }

    override fun getItemCount(): Int {
        return list.size
    }

    internal inner class SenderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun set(position: Int) {

            itemView.tv.text = list[position] + " " + position

        }
    }
}
