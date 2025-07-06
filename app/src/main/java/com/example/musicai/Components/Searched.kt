package com.example.musicai.Components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import com.example.musicai.adpter.SearchViewAdapter
import com.example.musicai.api.Constant
import io.ktor.http.*;
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Searched.newInstance] factory method to
 * create an instance of this fragment.
 */
class Searched(songs: List<Song>) : Fragment() {

    private var param1: String? = null
    private var param2: String? = null


    private val songs : List<Song> = songs;

    private lateinit var recyclerview : RecyclerView;
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
        return inflater.inflate(R.layout.fragment_searched, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            recyclerview = view.findViewById<RecyclerView>(R.id.recyclerViewMusic)
            recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
            recyclerview.adapter = SearchViewAdapter(songs);
        }



    }




    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String,songs: List<Song> ) =
            Searched(
                songs
            ).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }
}