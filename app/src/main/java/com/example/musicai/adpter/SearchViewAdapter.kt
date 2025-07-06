package com.example.musicai.adpter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import java.lang.Exception
import java.util.zip.Inflater

open class SearchViewAdapter(private  val songs: List<Song>)  : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): SearchViewHolder {
        // Inflate the layout for the SearchViewHolder and return a new instance
        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.search_item, parent, false)

        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        try {
            val currenSong = songs[position]
            holder.song_title.text = currenSong.title
            holder.song_artist.text = currenSong.artist
            holder.song_duration.text = currenSong.duration.toString()

            Glide.with(holder.itemView.context)
                .load(currenSong.coverUrl)
                .into(holder.song_cover)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in binding view holder: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        // Return the number of items in the dataset
        return songs.size
    }
}