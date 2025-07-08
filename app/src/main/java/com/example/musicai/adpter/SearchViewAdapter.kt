package com.example.musicai.adpter

import android.view.LayoutInflater
import android.widget.Toast
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

open class SearchViewAdapter(
    private val songs: List<Song>,
    private val tab: TabLayout,
    private val setUrl: (String) -> Unit,
    private val setSongid: (String) -> Unit,
    private val scope: LifecycleCoroutineScope
)  : RecyclerView.Adapter<SearchViewHolder>() {
    private val baseurl : String = Constant.baseurl

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
                    currenSong.id?.let {
                        setSongid(it)
                    }
                    currenSong.externalUrl?.let {
                        setUrl(it)
                    }
                    scope.launch {
                        saveUserCurrentSong(currenSong.id)
                    }
                    tab.getTabAt(2)?.select()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            holder.fav_btn.setOnClickListener {
                scope.launch {
                    try {
                        val userId = CurrentUser.id ?: "default_user_id"
                        favoriteSong(currenSong.id, userId, holder.itemView.context)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        println("Error adding to favorites: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in binding view holder: ${e.message}")
        }
    }

    suspend fun favoriteSong(songId: String, userId: String, context: android.content.Context) {
        try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }
            }
            val response = client.put("$baseurl/spotify/favorite/add/$userId") {
                contentType(ContentType.Application.Json)
                setBody(songId) // <-- chỉ truyền chuỗi
            }
            if (response.status.value == 200) {
                // Show Toast on main thread
                kotlinx.coroutines.Dispatchers.Main.let { mainDispatcher ->
                    kotlinx.coroutines.withContext(mainDispatcher) {
                        Toast.makeText(context, "Add to favorite successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                println("Song favorited successfully")
            } else {
                println("Failed to favorite song: ${response.status}")
            }
        } catch (e: kotlin.Exception) {
            e.printStackTrace()
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
        // Return the number of items in the dataset
        return songs.size
    }
}