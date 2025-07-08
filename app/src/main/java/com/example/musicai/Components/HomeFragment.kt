package com.example.musicai.Components

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.musicai.ClassProps.CurrentUser
import com.example.musicai.ClassProps.Song
import com.example.musicai.R
import com.example.musicai.api.Constant
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment(private val setUrl: (String) -> Unit, private val setSongid: (String) -> Unit) : Fragment(R.layout.fragment_home) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var baseurl = Constant.baseurl;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tab = view?.findViewById<com.google.android.material.tabs.TabLayout>(R.id.recomd_tab)
        println(tab)
        val search_bar = view.findViewById<EditText>(R.id.search_bar_text)

        search_bar.setOnClickListener {
            search_bar.post {
                search_bar.requestFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(search_bar, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        search_bar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                val query = search_bar.text.toString()

                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(search_bar.windowToken, 0)

                lifecycleScope.launch {
                    val songs = getSearchList(query);
                    childFragmentManager.beginTransaction().apply {
                        replace(R.id.tab_content_wrapper, Searched(songs,setUrl,setSongid)).commit();
                    }
                }


                true // đã xử lý
            } else {
                false
            }
        }



        childFragmentManager.beginTransaction().apply {
            replace(R.id.recomend_trending_frame, recomend(setUrl,setSongid))
            commit();
        }
    }

    fun changeBlankwithPercent (query: String): String{
        return query.replace(" ", "%20")
    }
    suspend  fun getSearchList( query: String ) : List<Song>
    {

        try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }
            }
            val response = client.get("$baseurl/spotify/search") {
                contentType(ContentType.Application.Json)
                parameter("query", changeBlankwithPercent(query) ?: "")
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

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)




    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String,setUrl: Unit,setSongid: (String) -> Unit) =
            HomeFragment({ setUrl }, {setSongid}).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun  setOnclikcFunction(tab: com.google.android.material.tabs.TabLayout) {

    }
}