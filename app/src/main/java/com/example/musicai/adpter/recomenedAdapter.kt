package com.example.musicai.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicai.ClassProps.CurrentUser
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import com.example.musicai.api.Constant
import com.google.android.material.tabs.TabLayout
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch
import java.lang.Exception

class recomenedAdapter(
    private val songs: List<Song>,
    private val tab: TabLayout,
    private val setUrl: (String) -> Unit,
    private val setSongid: (String) -> Unit,
    private val scope: LifecycleCoroutineScope
) : RecyclerView.Adapter<recommendViewHolder>() {

    private val baseurl: String = Constant.baseurl
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
                scope.launch {
                    saveUserCurrentSong(Currentsong.id ?: "", CurrentUser.id ?: "default_user_id")
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
    suspend fun saveUserCurrentSong(songid: String, userid: String = CurrentUser.id ?: "default_user_id") {
        try {
            val client = HttpClient(CIO){
                install(ContentNegotiation) {
                    gson()
                }
            }

            val response = client.put("${baseurl}/users/recent-plays/add/$userid") {
                contentType(io.ktor.http.ContentType.Application.Json)
                setBody(songid)
            }
            if (response.status == io.ktor.http.HttpStatusCode.OK) {
                println("Song saved successfully")
            } else {
                println("Failed to save song: ${response.status}")
            }
        } catch (e: kotlin.Exception) {

        }
    }

    override fun getItemCount(): Int {
       return songs.size
    }
}