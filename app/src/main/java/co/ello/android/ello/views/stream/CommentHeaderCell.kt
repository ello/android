package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.net.URL
import java.util.*


class CommentHeaderCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.comment_header_cell, parent, false)) {
    private val imageButton: ImageView = itemView.findViewById(R.id.imageView)
    private val usernameButton: Button = itemView.findViewById(R.id.usernameButton)
    private val cellButton: Button = itemView.findViewById(R.id.cellButton)
    private val timestampLabel: TextView = itemView.findViewById(R.id.timestampLabel)

    data class Config(
        val username: String?,
        val avatarURL: URL?,
        val postedAt: Date
        )

    init {
        imageButton.setOnClickListener { usernameButtonTapped() }
        usernameButton.setOnClickListener{ usernameButtonTapped() }
    }

    fun config(value: Config) {
        usernameButton.text = value.username ?: ""
        timestampLabel.text = value.postedAt.timeAgo()
        loadImageURL(value.avatarURL).transform(CircleTransform()).into(imageButton)
    }

    private fun usernameButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return
        val comment = item.model as? Comment ?: return
        val user = comment.author ?: return

        streamController.streamTappedUser(user)
    }
}
