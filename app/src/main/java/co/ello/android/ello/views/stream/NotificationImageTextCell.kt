package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import java.net.URL

class NotificationImageTextCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.notification_imagetext_cell, parent, false)) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val webView: WebView = itemView.findViewById(R.id.webView)

    data class Config(
            val imageURL: URL?,
            val text: String?
    )

    fun config(value : Config) {
        webView.webViewClient = PostTextCell.PostTextWebViewClient(webView)
        webView.loadUrl("about:blank")
        webView.loadData(value.text, "text/html; charset=utf-8", "UTF-8")
        loadImageURL(value.imageURL).into(imageView)
    }

}