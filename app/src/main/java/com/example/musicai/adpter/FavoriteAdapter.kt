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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoriteAdapter(
    private var songs: List<Song>,
    private val tab: TabLayout,
    private val setUrl: (String) -> Unit,
    private val setSongid: (String) -> Unit,
    private val scope: LifecycleCoroutineScope,
    private val reloadCallback: (() -> Unit)? = null


    ) : RecyclerView.Adapter<FavoriteViewHolder>() {
    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
        private  val baseurl : String = Constant.baseurl;
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.favorite_item, parent, false)

        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int
    ) {
        val currentSong = songs[position]
        holder.song_title.text = currentSong.title
        holder.song_artist.text = currentSong.artist
        holder.song_duration.text = currentSong.duration.toString()

        holder.del_btn.setOnClickListener {
            scope.launch {
                RemoveFromFavourite(currentSong.id ?: "", CurrentUser.id ?: "default_user_id")
                reloadCallback?.invoke()
            }
        }
        Glide.with(holder.itemView.context)
            .load(currentSong.coverUrl)
            .into(holder.song_cover)

        holder.play_btn.setOnClickListener {
            try {
                currentSong.id?.let {
                    setSongid(it)
                }
                currentSong.externalUrl.let {
                    setUrl(it)
                }
                scope.launch {
                    saveUserCurrentSong(currentSong.id);

                }
                tab.getTabAt(2)?.select()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
    suspend fun RemoveFromFavourite(songid: String, userid: String) {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response = client.put("$baseurl/users/favorite/remove/${userid}") {
            contentType(ContentType.Application.Json)
            setBody(songid)

        }
        if (response.status.value != 200) {
            throw Exception("Failed to remove song from favourites: ${response.status}")
        }else{
            println("Song removed from favourites successfully")

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
        return  songs.size
    }
}