package co.ello.android.ello

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.net.URL
import java.util.*

class NotificationImageTextCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.notification_imagetext_cell, parent, false)) {
    private val contentImageView: ImageView = itemView.findViewById(R.id.contentImageView)
    private val webView: WebView = itemView.findViewById(R.id.webView)
    private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)
    private val usernameButton: Button = itemView.findViewById(R.id.usernameButton)
    private val timestampLabel: TextView = itemView.findViewById(R.id.timestampLabel)
    private val actionableLayout : LinearLayout = itemView.findViewById(R.id.actionableLayout)
    private val contentLayout : LinearLayout = itemView.findViewById(R.id.contentLayout)

    init {
        userImageView.setOnClickListener { usernameButtonTapped() }
        usernameButton.setOnClickListener{ usernameButtonTapped() }
    }

    private fun usernameButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return
        val notification = item.model as? Notification ?: return
        val user = notification.author ?: return
        streamController.streamTappedUser(user)
    }

    data class Config(
            val notificationTitle: Spanned?,
            val avatarURL: URL?,
            val hasImage: Boolean,
            val contentImageURL: URL?,
            val contentText: String?,
            val postActionable: Boolean,
            val postedAt: Date
    )

    fun config(value : Config) {
        usernameButton.text = value.notificationTitle
        loadImageURL(value.avatarURL).transform(CircleTransform()).into(userImageView)

        if (value.contentText != null) {
            webView.webViewClient = PostTextCell.PostTextWebViewClient(webView)
            webView.loadUrl("about:blank")
            webView.loadData(value.contentText, "text/html; charset=utf-8", "UTF-8")
        } else {
            contentLayout.visibility = View.GONE
        }
        if (value.hasImage) {
            loadImageURL(value.contentImageURL).into(contentImageView)
        } else {

            contentImageView.visibility = View.GONE
        }

        if (!value.postActionable) {
            actionableLayout.visibility = View.GONE
        }
        timestampLabel.text = value.postedAt.timeAgo()
    }
}