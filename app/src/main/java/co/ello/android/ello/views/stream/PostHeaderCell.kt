package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.net.URL
import java.util.*


class PostHeaderCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.post_header_cell, parent, false)) {
    private val imageButton: ImageView
    private val usernameButton: Button
    private val cellButton: Button
    private val timestampLabel: TextView

    data class Config(
        val username: String?,
        val avatarURL: URL?,
        val postedAt: Date
        )

    init {
        imageButton = itemView.findViewById(R.id.imageView)
        cellButton = itemView.findViewById(R.id.cellButton)
        usernameButton = itemView.findViewById(R.id.usernameButton)
        timestampLabel = itemView.findViewById(R.id.timestampLabel)

        cellButton.setOnClickListener { cellTapped() }
        imageButton.setOnClickListener { usernameButtonTapped() }
        usernameButton.setOnClickListener{ usernameButtonTapped() }
    }

    var config: Config? = null
        set(value) {
            usernameButton.text = value?.username ?: ""
            timestampLabel.text = value?.postedAt?.timeAgo() ?: ""
            loadImageURL(value?.avatarURL).transform(CircleTransform()).into(imageButton)
        }

    private fun cellTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return
        val post = item.model as? Post ?: return

        streamController.streamTappedPost(post)
    }

    private fun usernameButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return
        val post = item.model as? Post ?: return
        val user = post.author ?: return

        streamController.streamTappedUser(user)
    }
}
