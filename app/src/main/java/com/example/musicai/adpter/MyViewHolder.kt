package com.example.musicai.adpter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicai.R
import org.w3c.dom.Text

 class MyViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
     val song_title : TextView = itemView.findViewById<TextView>(R.id.song_title)
     val song_album : TextView = itemView.findViewById<TextView>(R.id.song_album)

     val song_artist : TextView = itemView.findViewById<TextView>(R.id.song_artist)

     val song_duration : TextView = itemView.findViewById<TextView>(R.id.song_duration)

}