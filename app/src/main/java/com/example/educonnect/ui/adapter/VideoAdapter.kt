package com.example.educonnect.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.educonnect.data.Article
import com.example.educonnect.data.Video
import com.example.educonnect.databinding.ArticlesItemListBinding
import com.example.educonnect.databinding.VideoItemListBinding

class VideoAdapter(
    private val onItemClick: (Video) -> Unit
): RecyclerView.Adapter<VideoAdapter.ArticlesViewHolder>() {

    private val videoList = ArrayList<Video>()

    @SuppressLint("NotifyDataSetChanged")
    fun setVideoList(videoList: List<Video>) {
        this.videoList.clear()
        this.videoList.addAll(videoList)
        notifyDataSetChanged()
    }

    class ArticlesViewHolder(val binding: VideoItemListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(VideoItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(videoList[position].imageLink)
            .into(holder.binding.videoImg)

        holder.binding.videoTitle.text = videoList[position].title
        holder.binding.videoDescription.text = videoList[position].description
        holder.binding.videoPubDate.text = videoList[position].publishedTime

        holder.binding.root.setOnClickListener {
            onItemClick.invoke(videoList[position])
        }
    }


}