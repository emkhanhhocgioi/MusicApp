package com.example.musicai.Components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_URL = "url"

class AudioPlayer(private var songid: String) : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var urlEx: String? = null
    private lateinit var webView: WebView

    private val baseurl: String = Constant.baseurl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            urlEx = it.getString(ARG_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_audio_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favorite_button = view.findViewById<View>(R.id.btn_favorite)
        val btn_auto_next = view.findViewById<View>(R.id.btn_auto_next)

        webView = view.findViewById<WebView>(R.id.audio_webview)

        // Configure WebView settings
        configureWebView()

        // Load initial song
        loadSongInWebView()

        btn_auto_next.setOnClickListener {
            lifecycleScope.launch {
                val nextSong = getNextSong(CurrentUser.id ?: "")
                if (nextSong != null) {
                    // Update the song data
                    songid = nextSong.id ?: ""
                    urlEx = nextSong.externalUrl ?: ""

                    // Reload the WebView with the new song
                    loadSongInWebView()
                } else {
                    Toast.makeText(requireContext(), "No next song available", Toast.LENGTH_SHORT).show()
                }
            }
        }

        favorite_button.setOnClickListener {
            lifecycleScope.launch {
                favoriteSong(songid, CurrentUser.id ?: "")
            }
        }
    }

    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.setBackgroundColor(0x00000000)
        webView.webViewClient = WebViewClient()
    }

    private fun loadSongInWebView() {
        val iframeUrl = urlEx?.let { convertToEmbedUrl(it) }
            ?.let { "$it?autoplay=1" }
            ?: "https://open.spotify.com/embed/track/4nXrVH5xwN1w6TpmP7uu8n?autoplay=1"

        println("Loading song with URL: $iframeUrl")

        val htmlContent = """
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                <style>
                    html, body {
                        height: 100%;
                        margin: 0;
                        padding: 0;
                        background: #121212;
                    }
                    body {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        height: 100vh;
                    }
                    iframe {
                        border-radius: 12px;
                        border: none;
                        width: 100vw;
                        max-width: 400px;
                        height: 352px;
                        background: #121212;
                    }
                </style>
            </head>
            <body>
                <iframe src="$iframeUrl"
                        allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture"
                        allowtransparency="true">
                </iframe>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }

    fun convertToEmbedUrl(originalUrl: String): String? {
        if (!originalUrl.startsWith("https://open.spotify.com/")) return null

        return originalUrl
            .replace("https://open.spotify.com/", "https://open.spotify.com/embed/")
            .substringBefore("?")
    }

    suspend fun getNextSong(userId: String): Song? {
        return try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }
            }
            val response = client.get("$baseurl/spotify/next/song") {
                parameter("userid", userId)
            }

            client.close() // Close the client to free resources

            if (response.status == HttpStatusCode.OK) {
                response.body<Song>()
            } else {
                println("Failed to get next song: ${response.status}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error getting next song: ${e.message}")
            null
        }
    }

    suspend fun favoriteSong(songId: String, userId: String) {
        try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }
            }
            val response = client.put("$baseurl/spotify/favorite/add/$userId") {
                contentType(ContentType.Application.Json)
                setBody(songId)
            }

            client.close() // Close the client to free resources

            if (response.status == HttpStatusCode.OK) {
                // Show Toast on main thread
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Add to favorite successfully", Toast.LENGTH_SHORT).show()
                }
                println("Song favorited successfully")
            } else {
                println("Failed to favorite song: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error favoriting song: ${e.message}")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, url: String, songid: String) =
            AudioPlayer(songid).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_URL, url)
                }
            }
    }
}