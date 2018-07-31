package co.ello.android.ello

import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.net.URL
import java.util.*

class NotificationCell (parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.notification_cell, parent, false)) {
    private val imageButton: ImageView = itemView.findViewById(R.id.imageView)
    private val textButton: Button = itemView.findViewById(R.id.textButton)
    private val timestampLabel: TextView = itemView.findViewById(R.id.timestampLabel)

    data class Config(
            val username: String?,
            val avatarURL: URL?,
            val postedAt: Date
    )

    init {
        imageButton.setOnClickListener { usernameButtonTapped() }
        textButton.setOnClickListener{ usernameButtonTapped() }
    }

    fun config(value: Config) {
        textButton.text = generateText(value)
        timestampLabel.text = value.postedAt.timeAgo()
        loadImageURL(value.avatarURL).transform(CircleTransform()).into(imageButton)
    }

    private fun usernameButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return
        val notification = item.model as? Notification ?: return
        val user = notification.author ?: return

        streamController.streamTappedUser(user)
    }

    private fun generateText(value: Config) : Spanned? {
        val notification = streamCellItem?.model as? Notification ?: return null

        val text = when (notification.kind) {
            Notification.Kind.NewFollowedUserPost -> Html.fromHtml("You started following " +"<u>"+ value.username +"</u>.")
            Notification.Kind.NewFollowerPost -> Html.fromHtml("<u>"+ value.username +"</u> started following you.")
            else -> null
        }

        return text
    }
}
