package com.example.musicai.Components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicai.ClassProps.CurrentUser
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import com.example.musicai.adpter.SearchViewAdapter
import com.example.musicai.adpter.recomenedAdapter
import com.example.musicai.api.Constant
import com.google.android.material.tabs.TabLayout
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [recomend.newInstance] factory method to
 * create an instance of this fragment.
 */
class recomend (private val setUrl: (String) -> Unit,private  val setSongid: (String) -> Unit) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var baseurl : String = Constant.baseurl;



    private lateinit var recyclerview : RecyclerView;
    private lateinit var recyclerview2 : RecyclerView;
    var songs : List<Song> = emptyList<Song>() ;




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
        return inflater.inflate(R.layout.fragment_recomend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val maintabLayout = requireActivity().findViewById<TabLayout>(R.id.navigate_bar);
        try {

            recyclerview = view.findViewById<RecyclerView>(R.id.recomend_recycler_view)
            recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            recyclerview2 = view.findViewById<RecyclerView>(R.id.trending_recycler_view);
            recyclerview2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            lifecycleScope.launch {
                try {
                    val recentSongs = getListRecent()
                    val recomendSongs = getListRecommend();

                    songs = recentSongs
                    recyclerview.adapter = SearchViewAdapter(
                        songs,
                        maintabLayout,
                        setUrl,
                        setSongid,
                        lifecycleScope
                    )
                    recyclerview2.adapter = recomenedAdapter(recomendSongs,maintabLayout, setUrl,setSongid, lifecycleScope);
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Error loading songs: ${e.message}")
                }
            }


        }catch (e: Exception) {
            e.printStackTrace()
            println("Error in setting up RecyclerView: ${e.message}")
        }
    }
    suspend fun getListRecent(  ) : List<Song>
    {

        try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }
            }
            val response = client.get("$baseurl/spotify/recent") {
                contentType(ContentType.Application.Json)
                parameter("userid", CurrentUser.id.toString() ?: "")

            }
            if ( response.status == HttpStatusCode.OK ) {
                val songs = response.body<List<Song>>()
                return songs
            } else {
                println("Error: ${response.status}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in getting search list: ${e.message}")
        }

        return  null ?: emptyList<Song>()
    }

    suspend fun getListRecommend() : List<Song> {
        try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }
            }
            val response = client.get("$baseurl/spotify/recomended") {
                contentType(ContentType.Application.Json)
                parameter("userid", CurrentUser.id.toString() ?: "")
            }
            if (response.status == HttpStatusCode.OK) {
                val songs = response.body<List<Song>>()
                return songs
            } else {
                println("Error: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in getting recommend list: ${e.message}")
            return emptyList();
        }
        return emptyList<Song>();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment recomend.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, setUrl: (String) -> Unit , setSongid: (String) -> Unit) =
            recomend(setUrl, setSongid).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}