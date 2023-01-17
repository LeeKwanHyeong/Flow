package com.example.flow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.flow.databinding.MovieItemBinding
import com.example.flow.network.dto.response.ItemResponse
import com.example.flow.network.dto.response.MovieResponse
import kotlin.coroutines.CoroutineContext

class FlowMoviesAdapter: PagingDataAdapter<MovieResponse, FlowMoviesAdapter.FlowMoviesViewHolder>(
    MoviesComparator
) {
    private var itemClickCallback: FlowClickCallback? = null

    interface FlowClickCallback {
        fun itemClickListener(item: MovieResponse)
    }

    fun setItemClickListener(listener: FlowClickCallback){
        this.itemClickCallback = listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FlowMoviesViewHolder{
        return FlowMoviesViewHolder(MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FlowMoviesViewHolder, position: Int){
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }



    inner class FlowMoviesViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:  MovieResponse) = with(binding){
            binding.movieImage.apply {
                Glide.with(this)
                    .load(item.image)
                    .into(this)
            }

            val regex = Regex("<.*?>")
            binding.title.text = "제목 : ${item.title.replace(regex, "")}"
            binding.publish.text = "출시 : ${item.pubDate.replace(regex,"")}"
            binding.rate.text = "평점 : ${item.userRating}"

            binding.itemLayout.setOnClickListener {
                itemClickCallback!!.itemClickListener(item)
            }
        }
    }

    object MoviesComparator: DiffUtil.ItemCallback<MovieResponse>(){
        override fun areItemsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
            return oldItem == newItem
        }

    }
}