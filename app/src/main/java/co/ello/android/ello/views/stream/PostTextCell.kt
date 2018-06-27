package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class PostTextCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.post_text_cell, parent, false)) {
    val webView: WebView = itemView.findViewById(R.id.webView)

    data class Config(
        val content: String
        )

    class PostTextWebViewClient(val webView: WebView) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }
    }

    fun config(value: Config) {
        webView.webViewClient = PostTextWebViewClient(webView)
        webView.loadUrl("about:blank")
        webView.loadData(value.content, "text/html; charset=utf-8", "UTF-8")
    }
}
