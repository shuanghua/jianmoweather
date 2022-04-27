package dev.shuanghua.weather

import android.content.Context
import android.content.Intent
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL: String = "WEB_URL"
        const val EXTRA_TITLE: String = "TITLE"

        fun newIntent(
            content: Context,
            title: String,
            url: String
        ) = Intent(content, WebViewActivity::class.java).apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_URL, url)
        }
    }

    private var _url = ""
    private var _title = ""

    private lateinit var webView: WebView

}
