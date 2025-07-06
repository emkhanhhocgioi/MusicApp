package com.example.musicai.Components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.musicai.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_URL = "url"

class AudioPlayer : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var urlEx: String? = null

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

        val webView = view.findViewById<WebView>(R.id.audio_webview)
        val iframeUrl = urlEx?.let { convertToEmbedUrl(it) }
            ?: "https://open.spotify.com/embed/track/4nXrVH5xwN1w6TpmP7uu8n"

        val htmlContent = """
            <html>
            <body style="margin:0;padding:0;">
                <iframe src="$iframeUrl"
                        width="100%" height="80" frameborder="0"
                        allowtransparency="true"
                        allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture">
                </iframe>
            </body>
            </html>
        """.trimIndent()

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
        webView.webViewClient = WebViewClient()
    }

    fun convertToEmbedUrl(originalUrl: String): String? {
        if (!originalUrl.startsWith("https://open.spotify.com/")) return null

        return originalUrl
            .replace("https://open.spotify.com/", "https://open.spotify.com/embed/")
            .substringBefore("?")
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, url: String) =
            AudioPlayer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_URL, url)
                }
            }
    }
}
