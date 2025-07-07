package com.example.musicai.adpter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class recommendViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

      val title : TextView = itemView.findViewById<android.widget.TextView>(com.example.musicai.R.id.party_title)
      val artist : TextView = itemView.findViewById<android.widget.TextView>(com.example.musicai.R.id.party_subtitle)
      val cover : ShapeableImageView = itemView.findViewById<ShapeableImageView>(com.example.musicai.R.id.trending_image)
}