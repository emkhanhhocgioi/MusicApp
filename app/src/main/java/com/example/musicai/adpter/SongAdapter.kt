package com.example.musicai.adpter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import java.lang.Exception


open class SongAdapter (private  val songs: List<Song>) : RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        try {
            val currenSong = songs[position]
            holder.song_title.text = currenSong.title
            holder.song_album.text = currenSong.album
            holder.song_artist.text = currenSong.artist
            holder.song_duration.text = currenSong.duration.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in binding view holder: ${e.message}")
        }








    }

    override fun getItemCount(): Int {
        return  songs.size
    }


}