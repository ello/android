package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class NotificationHeaderCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.notification_header_cell, parent, false)) {
    private val imageButton: ImageView = itemView.findViewById(R.id.imageView)
    private val textButton: Button = itemView.findViewById(R.id.textButton)

    init {
        imageButton.setOnClickListener { usernameButtonTapped() }
        textButton.setOnClickListener{ usernameButtonTapped() }
    }

    fun config() {
        val notification = streamCellItem?.model as Notification
        textButton.text = NotificationAttributedTitle.from(notification = notification)
        loadImageURL(notification.author?.avatarURL()).transform(CircleTransform()).into(imageButton)
    }

    private fun usernameButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return
        val notification = item.model as? Notification ?: return
        val user = notification.author ?: return

        streamController.streamTappedUser(user)
    }
}
