package co.ello.android.ello

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout


class PostTextCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.post_text_cell, parent, false)) {
    val webView: WebView

    data class Config(
        val content: String
        )

    init {
        webView = itemView.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.visibility = View.VISIBLE
                webView.loadUrl("javascript:PostTextCell.resize(document.body.getBoundingClientRect().height)")
                super.onPageFinished(view, url)
            }
        }
        webView.addJavascriptInterface(this, "PostTextCell")
    }

    @JavascriptInterface
    fun resize(height: Float) {
        itemView.post({
            calculateHeight(height)
        })
    }

    var config: Config? = null
        set(value) {
            webView.visibility = View.INVISIBLE
            if (value == null)  return

            webView.loadUrl("about:blank")
            webView.loadData(value.content, "text/html; charset=utf-8", "UTF-8")
        }

    private fun assignHeight(height: Int) {
        webView.layoutParams = FrameLayout.LayoutParams(webView.measuredWidth, height)
    }

    private fun calculateHeight(height: Float) {
        if (streamCellItem?.height != null)  return
        assignHeight((10f + height).dp)
    }
}
