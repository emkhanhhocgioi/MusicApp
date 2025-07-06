package com.example.musicai.adpter

import android.media.Image
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicai.R
import org.w3c.dom.Text
import android.widget.ImageButton
class SearchViewHolder (itemView: View ) : RecyclerView.ViewHolder(itemView) {

    val song_title: TextView = itemView.findViewById<TextView>(R.id.textSongTitle)
    val song_artist: TextView = itemView.findViewById<TextView>(R.id.textArtistName)
    val song_cover : ImageView = itemView.findViewById<ImageView>(R.id.imageAlbumArt)
    val song_duration: TextView = itemView.findViewById<TextView>(R.id.textDuration)

    val play_btn : ImageButton = itemView.findViewById<ImageButton>(R.id.buttonPlay)




}