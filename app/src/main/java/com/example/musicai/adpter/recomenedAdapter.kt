package com.example.musicai.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import com.google.android.material.tabs.TabLayout
import java.lang.Exception

class recomenedAdapter(
    private val songs: List<Song>,
    private val tab: TabLayout,
    private val setUrl: (String) -> Unit,
    private val setSongid: (String) -> Unit,
    private val scope: LifecycleCoroutineScope
) : RecyclerView.Adapter<recommendViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): recommendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recomend_item, parent, false)
        return recommendViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: recommendViewHolder,
        position: Int
    ) {
        val Currentsong = songs[position]
        holder.title.text = songs[position].title
        holder.artist.text = songs[position].artist
        Glide.with(holder.itemView.context)
            .load(Currentsong.coverUrl)
            .into(holder.cover)
        holder.itemView.setOnClickListener {
            try {
                Currentsong.id?.let {
                    setSongid(it);
                }
                Currentsong.externalUrl?.let {
                    setUrl(it)
                }
                tab.getTabAt(2)?.select()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun getItemCount(): Int {
       return songs.size
    }
}