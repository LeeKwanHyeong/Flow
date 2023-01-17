package com.example.flow.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.flow.R
import com.example.flow.data.MovieModel
import com.example.flow.databinding.SearchedItemBinding

class FlowRecentSearchedAdapter(private val items: List<MovieModel>, private val context: Context): BaseAdapter() {

    private var itemClickCallback: FlowRecentClickCallback? = null

    interface FlowRecentClickCallback{
        fun itemClickListener(name: String)
    }

    fun setItemClickListener(listener: FlowRecentClickCallback){
        this.itemClickCallback = listener
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): MovieModel = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = SearchedItemBinding.inflate(LayoutInflater.from(context))

        val item = items[position]
        binding.searchedText.text = item.title

        binding.searchedListLayout.setOnClickListener {
            itemClickCallback!!.itemClickListener(item.title)
        }


        return binding.root
    }
}