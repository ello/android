package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import java.net.URL


class PostHeaderCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.post_header_cell, parent, false)) {
    private val imageView: ImageView
    private val usernameButton: Button
    private val timestampButton: Button

    data class Config(val username: String?, val avatarURL: URL?)

    init {
        imageView = itemView.findViewById(R.id.imageView)
        usernameButton = itemView.findViewById(R.id.usernameButton)
        timestampButton = itemView.findViewById(R.id.timestampButton)
    }

    var config: Config? = null
        set(value) {
            usernameButton.text = value?.username ?: ""
            loadImageURL(value?.avatarURL).transform(CircleTransform()).into(imageView)
        }
}
