package com.example.musicai.adpter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import com.google.android.material.tabs.TabLayout
import java.lang.Exception

open class SearchViewAdapter(
    private val songs: List<Song>,
    private val tab: TabLayout,
    private val setUrl: (String) -> Unit

    )  : RecyclerView.Adapter<SearchViewHolder>() {

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

            holder.play_btn.setOnClickListener {
                try {
                    currenSong.externalUrl?.let {
                        setUrl(it)
                    }

                    tab.getTabAt(2)?.select()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in binding view holder: ${e.message}")
        }
    }

    suspend fun saveUserCurrentSong (songid :String , userid: String) {

    }

    override fun getItemCount(): Int {
        // Return the number of items in the dataset
        return songs.size
    }
}