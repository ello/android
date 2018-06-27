package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import java.net.URL


class ProfileHeaderAvatarCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_avatar_cell, parent, false)) {
    val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
    val coverImageView: ImageView = itemView.findViewById(R.id.coverImageView)

    data class Config(
            var avatarURL: URL? = null,
            var coverImageURL: URL? = null
        )

    var config: Config? = null
        set(value) {
            loadImageURL(value?.avatarURL).transform(CircleTransform()).into(avatarImageView)
            coverImageView.setImageURL(value?.coverImageURL)
        }
}
