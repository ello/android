package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class ProfileHeaderBioCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_bio_cell, parent, false)) {
    val webView: WebView = itemView.findViewById(R.id.webView)

    data class Config(
        val content: String
        )

    var config: Config? = null
        set(value) {
            if (value == null)  return

            webView.webViewClient = PostTextCell.PostTextWebViewClient(webView)
            webView.loadUrl("about:blank")
            webView.loadData(value.content, "text/html; charset=utf-8", "UTF-8")
        }
}
