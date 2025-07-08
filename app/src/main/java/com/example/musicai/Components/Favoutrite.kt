package com.example.musicai.Components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicai.ClassProps.CurrentUser
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import com.example.musicai.adpter.FavoriteAdapter
import com.example.musicai.api.Constant
import com.google.android.material.tabs.TabLayout
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Favoutrite.newInstance] factory method to
 * create an instance of this fragment.
 */
class Favoutrite(
    private val setUrl: (String) -> Unit,
    private val setSongid: (String) -> Unit
) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val baseurl: String = Constant.baseurl
    private var favoriteAdapter: FavoriteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favoutrite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadFavoriteSongs()
    }

    private fun setupRecyclerView() {
        val favorite_rcv = view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.songsRecyclerView)
        favorite_rcv?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun loadFavoriteSongs() {
        lifecycleScope.launch {
            try {
                val songs = getFavouriteSongs()
                val mainTabLayout = requireActivity().findViewById<TabLayout>(R.id.navigate_bar)
                val favorite_rcv = view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.songsRecyclerView)

                favoriteAdapter = FavoriteAdapter(
                    songs,
                    mainTabLayout,
                    setUrl,
                    setSongid,
                    lifecycleScope,
                    ::reloadFavorites // Pass reload function to adapter
                )
                favorite_rcv?.adapter = favoriteAdapter
            } catch (e: Exception) {
                // Handle error - you might want to show a toast or error message
                e.printStackTrace()
            }
        }
    }

    // Function to reload favorites - can be called from adapter
    fun reloadFavorites() {
        lifecycleScope.launch {
            try {
                val songs = getFavouriteSongs()
                favoriteAdapter?.updateSongs(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadrecyclerView(songs: List<Song>, mainTabLayout: TabLayout) {
        val favorite_rcv = view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.songsRecyclerView)
        favorite_rcv?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        favoriteAdapter = FavoriteAdapter(
            songs,
            mainTabLayout,
            setUrl,
            setSongid,
            lifecycleScope,
            ::reloadFavorites
        )
        favorite_rcv?.adapter = favoriteAdapter
    }

    suspend fun getFavouriteSongs(): List<Song> {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response = client.get("$baseurl/spotify/favorite/get") {
            contentType(ContentType.Application.Json)
            parameter("id", CurrentUser.id.toString() ?: "")
        }
        if (response.status.value != 200) {
            throw Exception("Failed to fetch favourite songs: ${response.status}")
        }

        return response.body<List<Song>>()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Favoutrite.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, setUrl: (String) -> Unit, setSongid: (String) -> Unit) =
            Favoutrite(setUrl, setSongid).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}